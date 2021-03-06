package org.fkjava.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("org.fkjava")
@EnableJpaRepositories()//激活JPA的自动DAO扫描
public class StorageConfig {
	public static void main(String[] args) {
		SpringApplication.run(StorageConfig.class, args);
	}
}
