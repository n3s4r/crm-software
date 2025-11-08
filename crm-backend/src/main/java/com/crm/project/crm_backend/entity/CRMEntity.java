package com.crm.project.crm_backend.entity;

import com.crm.project.crm_backend.multitenancy.TenantFilter;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

/**
 * CRMEntity: Base class for all tenant-specific data (NFR-001).
 *
 * This class applies the Hibernate filter to every query involving inheriting entities,
 * ensuring data isolation between tenants.
 */
// Mark this class as a superclass whose fields will be included in child tables
@MappedSuperclass
// Define the filter name and parameter type/name
@FilterDef(name = TenantFilter.TENANT_FILTER_NAME,
           parameters = @ParamDef(name = TenantFilter.TENANT_PARAMETER_NAME, type = Long.class))
// Apply the filter to this entity, telling Hibernate to filter by tenant_id
@Filter(name = TenantFilter.TENANT_FILTER_NAME, condition = "tenant_id = :" + TenantFilter.TENANT_PARAMETER_NAME)
public abstract class CRMEntity {

    // This column MUST be present in every child table in the database
    @Column(name = "tenant_id", nullable = false, updatable = false)
    protected Long tenantId;
    
    // Optional: ManyToOne relationship back to the Tenant entity (good practice)
    @ManyToOne
    @JoinColumn(name = "tenant_id", insertable = false, updatable = false)
    protected Tenant tenant;

    // --- Getters and Setters ---
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}