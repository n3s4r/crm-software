package com.crm.project.crm_backend.multitenancy;

import java.util.UUID; // <-- IMPORT UUID

/**
 * This class holds the 'tenantId' for the current request.
 * It uses a ThreadLocal to ensure tenant data is isolated between concurrent requests.
 * * This version is updated to use UUID.
 */
public class TenantContext {

    // Use UUID instead of Long or String
    private static final ThreadLocal<UUID> currentTenant = new ThreadLocal<>();

    /**
     * Sets the tenant ID for the current request.
     * @param tenantId The UUID of the current tenant.
     */
    public static void setTenantId(UUID tenantId) {
        currentTenant.set(tenantId);
    }

    /**
     * Retrieves the tenant ID for the current request.
     * @return The UUID of the current tenant, or null if not set.
     */
    public static UUID getTenantId() {
        return currentTenant.get();
    }

    /**
     * Clears the tenant ID from the context, typically after the request is completed.
     */
    public static void clear() {
        currentTenant.remove();
    }
}
