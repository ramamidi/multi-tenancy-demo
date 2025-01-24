package com.multitenant.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.multitenant.demo.database.repositories"})
@ComponentScan(basePackages = {"com.multitenant.demo"})
public class MultiTenancyDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiTenancyDemoApplication.class, args);
	}

}
