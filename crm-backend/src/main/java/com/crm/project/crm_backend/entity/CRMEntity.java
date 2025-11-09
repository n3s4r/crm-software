package com.crm.project.crm_backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.UUID;

/**
 * This is the base class for ALL data entities in the CRM (Contacts, Leads, Tickets, etc.).
 * It automatically includes:
 * 1. A primary key (id).
 * 2. The 'tenant_id' field for multitenancy (using UUID).
 * 3. The Hibernate Filter definitions that automatically scope queries by tenant.
 *
 * By extending this class, new entities (like Contact) automatically
 * inherit all multitenancy security protections.
 */
@MappedSuperclass
// Define the Hibernate filter
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = UUID.class))
// Apply the filter globally to this entity and its children
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public abstract class CRMEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This is the multitenancy discriminator column.
    // It MUST match the Tenant's ID type (UUID).
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }
}
