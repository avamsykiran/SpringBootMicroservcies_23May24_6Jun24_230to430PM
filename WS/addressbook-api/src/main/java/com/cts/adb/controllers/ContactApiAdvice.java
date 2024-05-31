package com.cts.adb.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cts.adb.exceptions.InvalidEmployeeDetailsException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ContactApiAdvice {

	@ExceptionHandler(InvalidEmployeeDetailsException.class)
	public ResponseEntity<String> handleInvalidEmployeeDetailsException(InvalidEmployeeDetailsException exp){
		log.error(exp.getMessage(),exp);
		return new ResponseEntity<String>(exp.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleOtherException(Exception exp){
		log.error(exp.getMessage(),exp);
		return new ResponseEntity<String>("Something went wrong on the api side! Please retry later!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
