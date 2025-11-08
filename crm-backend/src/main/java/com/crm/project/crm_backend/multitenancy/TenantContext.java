package com.crm.project.crm_backend.multitenancy;

/**
 * TenantContext: A thread-local holder for the current Tenant ID.
 *
 * Spring Boot uses a separate thread to handle each incoming web request.
 * This class ensures that the 'tenantId' is isolated to the current request,
 * preventing data mix-ups between different tenants (NFR-001).
 *
 * This implementation uses Long, assuming tenant IDs are numeric primary keys.
 */
public class TenantContext {

    // ThreadLocal ensures data is only visible to the current thread/request.
    private static final ThreadLocal<Long> currentTenant = new ThreadLocal<>();

    /**
     * Sets the tenant ID for the current request thread.
     * @param tenantId The ID of the authenticated tenant.
     */
    public static void setTenantId(Long tenantId) {
        currentTenant.set(tenantId);
    }

    /**
     * Gets the tenant ID for the current request thread.
     * @return The current tenant's ID, or null if not set.
     */
    public static Long getTenantId() {
        return currentTenant.get();
    }

    /**
     * Clears the tenant ID from the thread after the request is complete.
     * CRITICAL to avoid data leakage if the thread is reused later.
     */
    public static void clear() {
        currentTenant.remove();
    }
}