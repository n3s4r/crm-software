package com.crm.project.crm_backend.service;

import com.crm.project.crm_backend.entity.Contact;
import com.crm.project.crm_backend.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }
    
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}

