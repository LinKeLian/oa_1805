package org.fkjava.identity.service.Impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.fkjava.identity.domain.Role;
import org.fkjava.identity.domain.User;
import org.fkjava.identity.domain.User.Status;
import org.fkjava.identity.repositry.RoleDao;
import org.fkjava.identity.repositry.UserDao;
import org.fkjava.identity.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
@Service
public class IndentityServiceImpl implements IdentityService{
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	@Transactional
	public void save(User user) {
		
		//处理角色：固定的始终有、角色不能重复
		//1、查询所有的固定的角色
		List<Role> fiexedRoles = roleDao.findByFixedTrue();
		
		//2、页面传入的角色
		List<Role> roles = user.getRoles();
		//不判断会出现空指针
		if(roles == null) {
			//不要相信页面的数据！
			//页面可能传了一个空的集合进来，所以需要在这里判断是否为空、如果为空则自己创建一个
			roles = new LinkedList<>();
			//不加下面这一行数据库保存不了
			user.setRoles(roles);
			
		}else {
			
			//根据页面传过来的id，查询所有的角色
			List<String> ids = new LinkedList<>();
			roles.stream()//转换为流式API
				.map((role) ->{
					return role.getId();
				}).forEach(id->{
					// 迭代每个id，把id添加到集合里面
					ids.add(id);
				});
			
//			for(Role r : roles) {
//			String id = r.getId();
//			ids.add(id);
//		}

//		roles.stream().map(role -> role.getId()).forEach(ids::add);
			List<Role> tmp = this.roleDao.findAllById(ids);
			//因为有可能是有相同的，所以要删除在添加
			roles.clear();
			roles.addAll(tmp);
			
			
			
		}
		//3、确保角色不能重复，需要重写Role的equals方法和hashCode方法，把所有角色添加到Set里面自然就不重复了！
		Set<Role> allRoles = new HashSet<>();
		allRoles.addAll(fiexedRoles);
		allRoles.addAll(roles);//set会去掉重复的角色
		
		//把整理后的角色放入User里面
		roles.clear();
		roles.addAll(allRoles);
		
		//1、检查是否没有id，如果没有id则直接把id设置为null，方便新增
		/**
		 * 因为在网页新增可能是空字符，所以排除
		 */
		if(StringUtils.isEmpty(user.getId())){
			user.setId(null);
			
		}
		
		//每次修改以后，都使用正常状态
		user.setStatus(Status.NORMAL);
		user.setExpiredTime(getExpireTime());
		
		
		//2、根登录名查询User对象，用于判断登录名是否被占用
		User old = userDao.findByLoginName(user.getLoginName());
		if(old == null) {
			
			if(!StringUtils.isEmpty(user.getId()) &&
					StringUtils.isEmpty(user.getPassword())) {
				// 如果是修改（有id），并且密码为空使用旧的密码
				user.setPassword(old.getPassword());
				
			}else {
				//使用新的密码
				String password = this.passwordEncoder.encode(user.getPassword());
				user.setPassword(password);
				
				
			}
			
			//没有登录名，为新增
			userDao.save(user);
		}else {
			//可能是修改的时候，登录名没有改变
			if(user.getId() != null && user.getId().equals(old.getId())) {
				//有id表示修改，并且页面传入的id跟数据库查询的id相同
				//此时表示：页面的登录名和数据库的登录名相同，但是是同一个用户
				if(StringUtils.isEmpty(user.getPassword())) {
					//当密码为空，则使用原来的密码
					user.setPassword(old.getPassword());
					
				}else {
					//使用新的密码
					String password = this.passwordEncoder.encode(user.getPassword());
					user.setPassword(password);
					
				}
				this.userDao.save(user);
				
			}else {
				//页面的登录与数据库的登录一致，但是不是同一用户，登录名被占用！
				//如果user的id为null的时候，表示新增，但是数据库有个同名的登录名
				throw new IllegalArgumentException("登录名重复！");
				
			}
			
			
		}
	}


	@Override
	public Page<User> findUsers(String keyword, Integer number) {
		if(StringUtils.isEmpty(keyword)) {
			
			keyword= null;
		}
		//分页查询的条件
		Pageable pageable = PageRequest.of(number, 4);
		
		Page<User> page;
		if(keyword == null) {
			//分页查询所有的数据
			page = this.userDao.findAll(pageable);
			
		}else {
			// 根据姓名查询，前后模糊查询
			page = this.userDao.findByNameContaining(keyword,pageable);
		}
		
		return page;
	}
	@Override
	public User findUserById(String id) {
		//orElse:如果没有从数据找到对象，
		return userDao.findById(id).orElse(null);
	}


	@Override
	@Transactional
	public void active(String id) {
		User user = this.userDao.findById(id).orElse(null);
		if(user != null) {
			user.setExpiredTime(getExpireTime());
			user.setStatus(User.Status.NORMAL);
		}
		
	}
	
	private Date getExpireTime() {
		//加上二个月
		Calendar cal = Calendar.getInstance();
		
		int month = cal.get(Calendar.MONTH);//获取月份
		month += 2;
		cal.set(Calendar.MONTH, month);// 把修改的月份设置回去！
		
		//2个月之后的时间
		Date time = cal.getTime();
		return time;
	}

	@Override
	@Transactional
	public void disable(String id) {
		User user = this.userDao.findById(id).orElse(null);
		if(user != null) {
			user.setStatus(User.Status.DISABLED);
		}
		
	}
	
	@Override
	public Optional<User> findByLoginName(String loginName) {
		User user = this.userDao.findByLoginName(loginName);
		// 把查询到的User转换为Optional
		Optional<User> op = Optional.ofNullable(user);
		return op;
	}
}
