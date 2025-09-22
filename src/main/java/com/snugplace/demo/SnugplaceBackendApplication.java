package com.snugplace.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("Model")
public class SnugplaceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnugplaceBackendApplication.class, args);
	}
}
