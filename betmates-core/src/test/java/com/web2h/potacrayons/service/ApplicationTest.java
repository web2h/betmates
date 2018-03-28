package com.web2h.potacrayons.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.web2h.potacrayons")
@EntityScan("com.web2h.potacrayons.model")
@ComponentScan("com.web2h.potacrayons")
public class ApplicationTest {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationTest.class, args);
	}
}