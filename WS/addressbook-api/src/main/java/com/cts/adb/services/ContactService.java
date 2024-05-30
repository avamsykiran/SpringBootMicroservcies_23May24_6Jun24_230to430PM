package com.cts.adb.services;

import java.util.List;

import com.cts.adb.entities.Contact;

public interface ContactService {

	List<Contact> getAll();
	Contact getById(long contactId);
	Contact addContact(Contact contact);
	Contact updateContact(Contact contact);
	void deleteById(long contactId);
}


