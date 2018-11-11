package org.fkjava.identity.controller;

import java.util.List;

import org.fkjava.identity.domain.Role;
import org.fkjava.identity.domain.User;
import org.fkjava.identity.service.IdentityService;
import org.fkjava.identity.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/identity/user")
@SessionAttributes({"modifyUserId"})
public class UserController {
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RoleService roleService;
	
	@GetMapping
	public ModelAndView index(//
			@RequestParam(name="pageNumber",defaultValue="0")Integer number,//页码
			@RequestParam(name="keyword",required=false)String keyword//搜索关键字
			) {
		ModelAndView mav = new ModelAndView("identity/user/index");
		//查询一页的数据
		Page<User> page = identityService.findUsers(keyword,number);
		mav.addObject("page", page);
		
		return mav;
	}
	
	@GetMapping("/add")
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("identity/user/add");
		// 后面这里需要查询数据，因为每个用户都有【身份】、【角色】，在修改用户信息的时候，需要选择用户的身份。
		
		List<Role> roles = this.roleService.findAllNotFixed();
		
		mav.addObject("roles", roles);
		
		return mav;
	}
	
	@PostMapping
	public String save(User user,//
			@SessionAttribute(value="modifyUserId",required=false)String modifyUserId,//
			//可以清除id
			SessionStatus sessionStatus) {
		//修改用户的时候，把用户的ID设置到User对象里面
		if(modifyUserId != null
				//user对象没有id代表新增，新增的时候不需要id
				&& !StringUtils.isEmpty(user.getId())) {
			user.setId(modifyUserId);
			
		}
		this.identityService.save(user);
		
		//清理现场、Session里面的modifyUserId
		sessionStatus.setComplete();
		
		return "redirect:/identity/user";
	}
	
	@GetMapping("/{id}")
	public ModelAndView detail(@PathVariable("id")String id) {
		ModelAndView mav = this.add();
		User user = this.identityService.findUserById(id);
		
		mav.addObject("user", user);
		//拿到用户的id用session保存起来
		mav.addObject("modifyUserId", user.getId());
		return mav;
	}
	/**
	 * 激活账户
	 * @param id
	 * @return
	 */
	@GetMapping("/active/{id}")
	public String active(@PathVariable("id") String id) {
		this.identityService.active(id);
		return "redirect:/identity/user";
	}
	/**
	 * 禁用账户
	 * @param id
	 * @return
	 */
	@GetMapping("/disable/{id}")
	public String disable(@PathVariable("id") String id) {
		this.identityService.disable(id);
		return "redirect:/identity/user";
	}
	
}
