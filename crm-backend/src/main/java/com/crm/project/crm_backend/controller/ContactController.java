package com.crm.project.crm_backend.controller;

import com.crm.project.crm_backend.entity.Contact;
import com.crm.project.crm_backend.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // GET /api/contacts - Returns all contacts for the current tenant
    @GetMapping
    public List<Contact> getContacts() {
        return contactService.getAllContacts();
    }

    // POST /api/contacts - Creates a new contact for the current tenant
    @PostMapping
    public Contact createContact(@RequestBody Contact contact) {
        return contactService.createContact(contact);
    }
    
    // DELETE /api/contacts/{id} - Deletes a contact
    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
    }
}

