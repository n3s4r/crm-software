package com.crm.project.crm_backend.repository;

import com.crm.project.crm_backend.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface that handles database operations for Contacts.
 * Because of our 'CRMEntity' and 'TenantFilter', 
 * findAll() will ONLY return contacts for the current tenant.
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    // We can add custom search methods here easily
    List<Contact> findByEmail(String email);
}
