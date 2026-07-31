package com.Subhojit.Hire_Hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class HireHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(HireHubApplication.class, args);
	}

}
