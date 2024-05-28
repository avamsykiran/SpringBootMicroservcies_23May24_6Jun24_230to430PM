package com.cts.sba.utility;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Counter {

	private int seed;
	
	public int next() {
		return ++seed;
	}
}
