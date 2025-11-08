package com.crm.project.crm_backend.multitenancy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * TenantIdFilter: Extracts the Tenant ID and sets it in the TenantContext.
 *
 * This runs at the beginning of every request to ensure the tenant ID is set
 * before any business logic or database access occurs.
 */
@Component
@Order(1) // Ensure this runs very early in the filter chain
public class TenantIdFilter extends OncePerRequestFilter {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // --- STEP 1: Extract Tenant ID ---
            String tenantIdStr = request.getHeader(TENANT_HEADER);

            if (tenantIdStr != null && !tenantIdStr.isEmpty()) {
                try {
                    Long tenantId = Long.parseLong(tenantIdStr);
                    // --- STEP 2: Store in Thread-Local Context ---
                    TenantContext.setTenantId(tenantId);
                    System.out.println("Set Tenant ID for request: " + tenantId); // Debugging
                } catch (NumberFormatException e) {
                    System.err.println("Invalid Tenant ID format in header: " + tenantIdStr);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Tenant ID format");
                    return;
                }
            } else {
                // For paths like /api/login or /api/register, a Tenant ID might not be needed yet.
                // You will refine this later to enforce the Tenant ID check on all secured endpoints.
                System.out.println("Warning: No " + TENANT_HEADER + " header found.");
            }

            // --- STEP 3: Continue the Filter Chain ---
            // Allows the request to proceed to Spring Security and controllers
            filterChain.doFilter(request, response);

        } finally {
            // --- STEP 4: Cleanup (CRITICAL) ---
            // Must clear the ID when the request is done to prevent leakage to subsequent requests.
            TenantContext.clear();
        }
    }
}