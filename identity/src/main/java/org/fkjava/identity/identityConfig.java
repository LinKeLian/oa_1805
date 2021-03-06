package org.fkjava.identity;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 针对Identity模块的配置
 * @author Administrator
 *
 */
@SpringBootApplication
@ComponentScan("org.fkjava")
@EnableJpaRepositories()//激活JPA的自动DAO扫描
public class identityConfig implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//通过拦截器统一设置ctx变量，这样页面都可以直接使用
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
					throws Exception {
				
				request.getServletContext().setAttribute("ctx", request.getContextPath());
				
				return true;
			}
		}).addPathPatterns("/*");
		
		
	}
	
	
		@Bean
		public PasswordEncoder passwordEncoder() {
			//PasswordEncoder encoder = new BCryptPasswordEncoder();
			//PasswordEncoder encoder = new SCryptPasswordEncoder();
			Map<String,PasswordEncoder> map = new HashMap<>();
			map.put("B", new BCryptPasswordEncoder());
			map.put("S", new SCryptPasswordEncoder());
			
			// 第一个参数表示默认的编码器
			DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder("B", map);
			// 在没有找到密码的加密器的时候，使用BCryptPasswordEncoder
			//encoder.setDefaultPasswordEncoderForMatches(map.get("B"));
			
			return encoder;
		}
	
	public static void main(String[] args) {
		SpringApplication.run(identityConfig.class, args);
	}
}
