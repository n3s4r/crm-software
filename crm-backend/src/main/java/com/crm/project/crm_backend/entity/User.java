package com.crm.project.crm_backend.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Represents an authenticated user of the system.
 * Each user is directly linked to ONE tenant via the 'tenantId'.
 * This enforces the "1 person 1 login" and multitenancy link.
 * * This version includes the 'role' field.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username; // e.g., email address

    @Column(name = "password_hash", nullable = false)
    private String password; // Will store the BCrypt hash

    // Link to the Tenant. This MUST match the Tenant's ID type.
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    // The 'role' field that was missing
    @Column(name = "role", nullable = false)
    private String role; // e.g., "ADMIN", "USER"

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

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    // Getter for the new 'role' field
    public String getRole() {
        return role;
    }

    // Setter for the new 'role' field
    public void setRole(String role) {
        this.role = role;
    }
}
