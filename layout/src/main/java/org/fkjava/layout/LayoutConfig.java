package org.fkjava.layout;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;

import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan("org.fkjava")
public class LayoutConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	// 在Spring Boot里面增加自定义的过滤器
	// 只需要的Bean的类型为FilterRegistrationBean，那么就会自动作为一个过滤器增加到容器里面
	@Bean
	public FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter() {
//		ConfigurableSiteMeshFilter filter = new ConfigurableSiteMeshFilter() {
//			@Override
//			public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
//					FilterChain filterChain) throws IOException, ServletException {
//				HttpServletRequest request = (HttpServletRequest) servletRequest;
//				System.out.println("过滤器被调用: " + request.getRequestURI());
//				super.doFilter(servletRequest, servletResponse, filterChain);
//			}
//		};

		ConfigurableSiteMeshFilter filter = new ConfigurableSiteMeshFilter();

		FilterRegistrationBean<ConfigurableSiteMeshFilter> bean = new FilterRegistrationBean<>();
		// 这种过滤器的本质，是拦截器，Spring Boot里面，所有的静态文件都不会进入Spring MVC中
		// 所以根本无法把js、css拦截到！
		bean.addUrlPatterns("/*");// 拦截所有的URL
		bean.setFilter(filter);// 把过滤器加入Spring里面
		bean.setAsyncSupported(true);// 激活异步Servlet请求支持
		bean.setEnabled(true);// 激活使用
		// 只处理来自浏览器的请求和错误的转发，Forward、include等请求都不处理
		bean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);

		// 初始化过滤器的参数
		Map<String, String> initParameters = new HashMap<>();
		// /*使用main.jsp来装饰
		// /admin/* 使用admin.jsp来装饰
		initParameters.put("decoratorMappings", "/*=/WEB-INF/layouts/main.jsp\n/admin/*=/WEB-INF/layouts/admin.jsp");
		// 排除某些路径不要装饰
		//initParameters.put("exclude", "/identity/role,/identity/role/*");

		bean.setInitParameters(initParameters);

		return bean;
	}

	public static void main(String[] args) {
		SpringApplication.run(LayoutConfig.class, args);
	}
}
