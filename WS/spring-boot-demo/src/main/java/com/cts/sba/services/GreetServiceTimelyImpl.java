package com.cts.sba.services;

import java.time.LocalTime;

import org.springframework.stereotype.Service;

@Service
public class GreetServiceTimelyImpl implements GreetService {

	@Override
	public String greetUser(String userName) {
		String greeting="";
		
		int hour = LocalTime.now().getHour();
		
		if(hour>=3 && hour<=11) greeting = "Good Morning";
		else if(hour<=16) greeting = "Good Noon";
		else greeting="Good Evening";
		
		return String.format("%s %s!", greeting, userName);
	}

}
