package org.fkjava.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fkjava.security.interceptors.UserHolderInterceptor;
import org.fkjava.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan("org.fkjava")
@EnableJpaRepositories
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer{
	@Autowired
	private SecurityService securityService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 不要调用super.configure(auth)方法
				// 如果调用了，Spring会自动创建一个DaoAuthenticationProvider
				// 具体创建的地方在InitializeUserDetailsBeanManagerConfigurer类里面
				// 代码执行路径是从WebSecurityConfigurerAdapter.authenticationManager()进去的。
//				super.configure(auth);
		
		// 此时DaoAuthenticationProvider不会被Spring容器管理，而是直接注入到AuthenticationManagerBuilder里面
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setHideUserNotFoundExceptions(false);
		provider.setUserDetailsService(securityService);
		provider.setPasswordEncoder(passwordEncoder);
		auth.authenticationProvider(provider);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new UserHolderInterceptor())//
			.addPathPatterns("/**")//
			//默认Spring Security的拦截器，已经在其他的拦截器之前
			//所以不使用order也是可以有效的
			//如果不能正常获取到User（通过UserHolder），那么就需要修改顺序
			//.order(Integer.MAX_VALUE)//排到最后面
		;
	}
	
	
	
	//配置基于HTTP的安全控制
	/**
	 * 用户名可以回显
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String loginPage = "/security/login";
		//处理登录失败的时候的任务
		SimpleUrlAuthenticationFailureHandler failureHandler = 
				new SimpleUrlAuthenticationFailureHandler(loginPage + "?error") {
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				
				request.getSession().setAttribute("loginName", request.getParameter("loginName"));
				//System.out.println(request.getParameter("loginName"));
				//在重定向之前，先把登录名放到Session
				super.onAuthenticationFailure(request, response, exception);
			}
		};
		
		http.authorizeRequests()//验证请求
				// 登录页面的地址和其他的静态页面都不要权限
				// /*表示目录下的任何地址，但是不包括子目录
				// /** 则连同子目录一起匹配
				.antMatchers(loginPage, "/css/**","/js/**", "/webjars/**", "/static/**")//
				.permitAll()//不做访问判断
				.anyRequest()//所有的请求
				.authenticated()//授权以后才能访问
				.and()//并且
				.formLogin()//表单验证
				.loginPage(loginPage)// 登录页面的位置，默认是/login
				// 此页面不需要有对应的JSP，而且也不需要有对应代码，只要URL
				// 这个URL是Spring Security使用的，用来接收请求参数、调用Spring Security的鉴权模块
				.loginProcessingUrl("/security/do-login")// 处理登录请求的URL
				// 在登录成功以后，会判断Session里面是否有记录之前访问的URL，如果有则使用之前的URL继续访问
				// 如果没有则使用defaultSuccessUrl
				// .defaultSuccessUrl("/index")//默认的登录成功页面
				.usernameParameter("loginName")// 登录名的参数名
				.passwordParameter("password")// 密码的参数名称
				.failureHandler(failureHandler)//这是可以把密码回显
				.and().logout()//配置退出登录
				.logoutUrl("/security/do-logout")
				// .logoutSuccessUrl("/")
				// .and().httpBasic()// 也可以基于HTTP的标准验证方法（弹出对话框）
				.and().csrf()// 激活防跨站攻击功能
		;
				
				
	}
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//系统默认自动把静态文件的根目录，映射到/public、/static、/resources里面
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// 动态注册URL和视图的映射关闭，解决控制器里面几乎没有代码的问题。
		registry.addViewController("/security/login")//
				.setViewName("security/login");
		//添加首页
		registry.addViewController("/idenx").setViewName("security/index");
		// 欢迎页，访问根目录重定向到一个首页
		// registry.addViewController("/").setViewName("security/index");
		registry.addRedirectViewController("/", "/index");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SecurityConfig.class, args);
	}
	
}
