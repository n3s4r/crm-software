package com.crm.project.crm_backend.multitenancy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID; // <-- IMPORT UUID

/**
 * Spring Web Filter that intercepts every request to extract the 'X-Tenant-ID' header.
 * It then sets the tenant ID in the TenantContext for the current thread.
 * * This version is updated to parse and set a UUID.
 */
@Component
public class TenantIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // --- STEP 1: Extract Tenant ID from Request Header ---
        String tenantIdStr = request.getHeader("X-Tenant-ID");

        if (tenantIdStr != null && !tenantIdStr.isEmpty()) {
            try {
                // --- STEP 2: Parse as UUID (not Long) ---
                UUID tenantId = UUID.fromString(tenantIdStr);

                // --- STEP 3: Store in Thread-Local Context (now accepts UUID) ---
                TenantContext.setTenantId(tenantId);
                
                System.out.println("TenantIdFilter: Set Tenant ID to " + tenantId);

            } catch (IllegalArgumentException e) {
                // Catch exception for invalid UUID format
                System.err.println("Invalid Tenant ID format in header: " + tenantIdStr);
                // Optionally, you could return an HTTP 400 Bad Request error here
                // response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                // response.getWriter().write("Invalid Tenant ID format");
                // return;
            }
        } else {
             // This else block is important for requests that DON'T have a tenant ID,
             // like the login page itself.
             System.out.println("TenantIdFilter: No X-Tenant-ID header found.");
        }

        try {
            // --- STEP 4: Continue the filter chain ---
            filterChain.doFilter(request, response);
        } finally {
            // --- STEP 5: Clear context after request is complete ---
            TenantContext.clear();
            System.out.println("TenantIdFilter: Cleared Tenant context.");
        }
    }

    /**
     * This method ensures the filter is NOT applied to the login endpoint,
     * as the tenant ID is not known until after login.
     * We will configure this properly in SecurityConfig later.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Example: Don't filter the login URL
        // return request.getRequestURI().equals("/api/auth/login");
        
        // For now, let's filter all, but be aware we'll need to refine this
        // for the login page, which won't have the X-Tenant-ID header.
        
        // **UPDATE:** The login process (CustomUserDetailsService) sets the tenant ID,
        // so this filter is actually for *subsequent* requests (after login).
        // Let's refine this to skip login.
        
        String path = request.getRequestURI();
        return path.equals("/api/auth/login") || path.equals("/api/auth/register");
    }
}
