package com.leonel.damespielServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class DamespielServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DamespielServerApplication.class, args);
	}

}
