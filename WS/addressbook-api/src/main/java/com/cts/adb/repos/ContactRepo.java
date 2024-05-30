package com.cts.adb.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.adb.entities.Contact;

public interface ContactRepo extends JpaRepository<Contact, Long> {

}
