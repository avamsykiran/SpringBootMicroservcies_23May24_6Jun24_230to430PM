package com.cts.adb.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.adb.entities.Contact;
import com.cts.adb.repos.ContactRepo;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactRepo contactRepo;
	
	@Override
	public List<Contact> getAll() {
		return contactRepo.findAll();
	}

	@Override
	public Contact getById(long contactId) {
		return contactRepo.findById(contactId).orElse(null);
	}

	@Override
	public Contact addContact(Contact contact) {
		return contactRepo.save(contact);
	}

	@Override
	public Contact updateContact(Contact contact) {
		if(contactRepo.existsById(contact.getContactId())) {
			contact = contactRepo.save(contact);
		}
		return contact;
	}

	@Override
	public void deleteById(long contactId) {
		contactRepo.deleteById(contactId);
	}

}
