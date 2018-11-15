package org.fkjava.security.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fkjava.identity.UserHolder;
import org.fkjava.identity.domain.User;
import org.fkjava.security.domain.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserHolderInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//获取Spring Security 里面保存的UserDetails对象，并且转换为User存储到UserHolder里面
		
		if(SecurityContextHolder//
				.getContext()//
				.getAuthentication() == null) {
			return true;
		}
		
		Object principal = SecurityContextHolder//
				.getContext()//
				.getAuthentication()//
				.getPrincipal();
		// Spring Security在没有登录的时候，会把当前用户设置为【匿名用户】
		// anonymous
		if (principal instanceof UserDetails) {
			UserDetails details = (UserDetails) principal;
 			User user = new User();
			user.setId(details.getUserId());
			user.setName(details.getName());
 			UserHolder.set(user);
		}
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 清理现场
		UserHolder.remove();
	}
	
}	
