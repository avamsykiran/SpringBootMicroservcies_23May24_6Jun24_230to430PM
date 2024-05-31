package com.cts.adb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.adb.entities.Contact;
import com.cts.adb.exceptions.InvalidEmployeeDetailsException;
import com.cts.adb.services.ContactService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/contacts")
public class ContactApiController {

	@Autowired
	private ContactService contactService;

	@GetMapping
	public ResponseEntity<List<Contact>> getAllContacts() {
		return ResponseEntity.ok(contactService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Contact> getContactById(@PathVariable("id") int contactId) {
		Contact contact = contactService.getById(contactId);
		return contact == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(contact);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteContactById(@PathVariable("id") int contactId) {
		contactService.deleteById(contactId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping
	public ResponseEntity<Contact> addContact(@RequestBody @Valid Contact contact, BindingResult result)
			throws InvalidEmployeeDetailsException {
		if (result.hasErrors()) {
			throw new InvalidEmployeeDetailsException(
				result.getAllErrors().stream().map(ObjectError::getDefaultMessage).reduce((m1, m2) -> m1 + "," + m2).orElse(""));
		}

		return new ResponseEntity<>(contactService.addContact(contact), HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Contact> updateContact(@RequestBody @Valid Contact contact, BindingResult result)
			throws InvalidEmployeeDetailsException {
		if (result.hasErrors()) {
			throw new InvalidEmployeeDetailsException(
				result.getAllErrors().stream().map(ObjectError::getDefaultMessage).reduce((m1, m2) -> m1 + "," + m2).orElse(""));
		}

		return new ResponseEntity<>(contactService.updateContact(contact), HttpStatus.ACCEPTED);
	}
}
