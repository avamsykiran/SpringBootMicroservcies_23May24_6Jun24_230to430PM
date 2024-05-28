package com.cts.sba;

import java.time.LocalDate;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootDemoApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootDemoApplication.class, args);
		
	}

	@Bean
	public Scanner scan() {
		return new Scanner(System.in);
	}
	
	@Bean
	public LocalDate today() {
		return LocalDate.now();
	}
}
