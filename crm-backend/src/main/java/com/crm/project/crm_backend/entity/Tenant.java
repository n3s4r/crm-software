package com.crm.project.crm_backend.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Represents a Tenant (e.g., a company) using the CRM.
 * This entity's primary key (id) will be the 'tenant_id'
 * used by all other entities.
 */
@Entity
@Table(name = "tenants")
public class Tenant {

    @Id
    @Column(name = "tenant_id")
    private UUID tenantId; // Use UUID as the primary key

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    // --- Constructors ---

    public Tenant() {
    }

    public Tenant(UUID tenantId, String name) {
        this.tenantId = tenantId;
        this.name = name;
    }

    // --- Getters and Setters ---

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
