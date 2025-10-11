package com.snugplace.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan("com.snugplace.demo.Model")
@EnableScheduling
public class SnugplaceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnugplaceBackendApplication.class, args);
	}
}
