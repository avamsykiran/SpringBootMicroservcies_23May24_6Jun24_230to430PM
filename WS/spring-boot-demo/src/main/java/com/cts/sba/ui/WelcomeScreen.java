package com.cts.sba.ui;

import java.time.LocalDate;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cts.sba.models.Employee;
import com.cts.sba.services.GreetService;
import com.cts.sba.utility.Counter;

@Component
public class WelcomeScreen implements CommandLineRunner {

	@Autowired
	private LocalDate date;
	
	@Autowired
	private Scanner kbin;
	
	@Autowired
	@Qualifier("greetServiceSimpleImpl")
	private GreetService greetService1;
	
	@Autowired
	@Qualifier("greetServiceTimelyImpl")
	private GreetService greetService2;

	@Autowired
	private Counter c1;
	
	@Autowired
	private Counter c2;
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hello! This is my first Spring Boot Application - Welcome Screen.");
		System.out.println(date);
		System.out.println("-------------------------------------------------------------------------------");
		
		System.out.println("User Name? ");
		String userName = kbin.nextLine();
		
		System.out.println(greetService1.greetUser(userName));
		System.out.println(greetService2.greetUser(userName));
		
		System.out.println(c1.next());
		System.out.println(c1.next());
		System.out.println(c1.next());
		System.out.println(c2.next());
		System.out.println(c2.next());
		System.out.println(c2.next());
		
		System.out.println(new Employee(101,"Vamsy Kiran",45000.0));
		System.out.println(new Employee(102,"Sagar Guru Charan",65000.0));
	}

}
