package org.fkjava.menu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories//激活扫描包
@ComponentScan("org.fkjava")//Identity模块的包也会被扫描
public class MenuConfig {
	
	public static void main(String[] args) {
		SpringApplication.run(MenuConfig.class, args);
	}
}
