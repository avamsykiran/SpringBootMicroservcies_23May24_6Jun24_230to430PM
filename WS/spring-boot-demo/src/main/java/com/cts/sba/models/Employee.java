package com.cts.sba.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Employee {

	private int empId;
	private String fullName;
	private double salary;
	
}
