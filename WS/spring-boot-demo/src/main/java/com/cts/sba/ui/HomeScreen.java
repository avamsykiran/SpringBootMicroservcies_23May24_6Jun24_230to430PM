package com.cts.sba.ui;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HomeScreen implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hello! This is my first Spring Boot Application - Home Screen.");
	}

}