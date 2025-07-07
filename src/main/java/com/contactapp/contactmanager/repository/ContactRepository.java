package com.contactapp.contactmanager.repository;

import com.contactapp.contactmanager.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
