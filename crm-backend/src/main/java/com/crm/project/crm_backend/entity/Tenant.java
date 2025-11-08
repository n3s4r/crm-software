package com.crm.project.crm_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Tenant: Represents a single, isolated customer/business unit (NFR-001).
 *
 * This table will store the mapping of which ID corresponds to which tenant.
 */
@Entity
@Table(name = "tenants")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // This is the 'tenant_id' used everywhere
    
    private String name; // The name of the company/tenant
    
    private String contactEmail;

    // --- Constructors ---
    public Tenant() {}

    public Tenant(String name, String contactEmail) {
        this.name = name;
        this.contactEmail = contactEmail;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}