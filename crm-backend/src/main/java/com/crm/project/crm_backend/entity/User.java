package com.crm.project.crm_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * User: Represents a single login with a 1:1 link to a specific tenant (NFR-003).
 *
 * This entity is NOT tenant-isolated via the filter, as we need to find the user
 * by username/password BEFORE we know their tenantId.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username"}) // Ensures no two users share a username
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String username; // The login ID
    
    @Column(nullable = false)
    private String password; // Hashed password
    
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId; // The crucial link to the user's company/tenant (NFR-001)

    // A simple role system for authorization (e.g., ADMIN, SALES_REP)
    @Column(nullable = false)
    private String role; 

    // --- Constructors ---
    public User() {}

    public User(String username, String password, Long tenantId, String role) {
        this.username = username;
        this.password = password;
        this.tenantId = tenantId;
        this.role = role;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}