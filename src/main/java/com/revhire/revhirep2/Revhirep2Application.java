package com.revhire.revhirep2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class Revhirep2Application {

	public static void main(String[] args) {
		SpringApplication.run(Revhirep2Application.class, args);
	}

}
