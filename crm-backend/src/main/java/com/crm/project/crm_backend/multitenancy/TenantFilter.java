package com.crm.project.crm_backend.multitenancy;

import org.springframework.stereotype.Component;

/**
 * TenantFilter: Simple class to hold the constant names for the Hibernate filter.
 *
 * This allows us to refer to the filter name and parameter name consistently
 * across the MultitenancyConfiguration and our Entity classes.
 */
@Component
public class TenantFilter {
    public static final String TENANT_FILTER_NAME = "tenantFilter";
    public static final String TENANT_PARAMETER_NAME = "tenantId";
}