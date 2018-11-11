package org.fkjava.identity.service.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.fkjava.identity.domain.Role;
import org.fkjava.identity.repositry.RoleDao;
import org.fkjava.identity.service.RoleService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RoleServiceImpl implements RoleService,InitializingBean{

	@Autowired
	private RoleDao roleDao;
	
	
	@Override
	@Transactional
	public void afterPropertiesSet() throws Exception {
		// 在服务启动的时候，检查是否有预置的角色,如果没有则自动加上
		Role admin = this.roleDao.findByRoleKey("ADMIN")// 需要手动加入给用户的
				.orElse(new Role());
		admin.setName("超级管理员");
		admin.setRoleKey("ADMIN");
		this.save(admin);

		// 所有用户的默认角色
		// 通过findByRoleKey查询对象
		Role user = this.roleDao.findByRoleKey("USER")//
				// 如果没有查询到，则new一个
				.orElse(new Role());
		user.setName("普通用户");
		user.setRoleKey("USER");
		user.setFixed(true);// 固定的、每个用户都具有的
		this.save(user);
	}
	
	@Override
	public List<Role> findAllRoles() {
		return this.roleDao.findAll();
	}

	@Override
	public void save(Role role) {
		if (StringUtils.isEmpty(role.getId())) {
			role.setId(null);
		}
		//查询role的key值
		Role old = roleDao.findByRoleKey(role.getRoleKey()).orElse(null);
		if (
		// id没有表示新增，old是空的，表示数据库没有重复的角色
		(role.getId() == null && old == null)
				// 修改的时候，根据key从数据库找到一个Role，但是id相同，此时只是修改了名称、没有修改key
				|| (role.getId() != null && old != null && role.getId().equals(old.getId()))//
				// 修改，但是key从数据库里面没有找到记录，表示key也修改了！
				|| (role.getId() != null && old == null)) {
			roleDao.save(role);
		} else {
			// 要么id为空、数据库不为空
			// 要么id不为空、数据库也不为空，但是id不相同
			throw new IllegalArgumentException("角色的KEY是唯一的，不能重复！");
		}
	}

	@Override
	public void deleteById(String id) {
		roleDao.deleteById(id);
		
	}

	@Override
	public List<Role> findAllNotFixed() {
		
		return this.roleDao.findByFixedFalseOrderByName();
	}

	@Override
	public List<Role> findAll() {
		
		return this.roleDao.findAll();
	}
}
