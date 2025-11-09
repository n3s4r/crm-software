package com.crm.project.crm_backend.config;

import com.crm.project.crm_backend.multitenancy.TenantIdFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder; // <--- ADD BOTH OF THESE IMPORTS
/**
 * SecurityConfig: Configuration for Spring Security (NFR-002).
 *
 * This configuration enables security and integrates our custom TenantIdFilter.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TenantIdFilter tenantIdFilter;

    // Inject our custom filter
    public SecurityConfig(TenantIdFilter tenantIdFilter) {
        this.tenantIdFilter = tenantIdFilter;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	// BCrypt is the industry standard for securely hashing passwords
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF (Common for stateless REST APIs)
            .csrf(AbstractHttpConfigurer::disable)

            // 2. Add our custom Tenant Filter BEFORE the main security filters
            // This ensures the Tenant ID is set even before authentication
            .addFilterBefore(tenantIdFilter, UsernamePasswordAuthenticationFilter.class)

            // 3. Authorization Rules
            .authorizeHttpRequests(authorize -> authorize
                // Allow anyone to access the /public or /auth endpoints
                .requestMatchers("/api/auth/**", "/public/**").permitAll()
                // All other API requests must be authenticated (we'll implement this later)
                .requestMatchers("/api/**").authenticated()
                // Require authentication for any other request
                .anyRequest().authenticated()
            );

        // NOTE: Later, we will add formLogin() or httpBasic() for basic authentication,
        // but for now, we just set up the filter chain structure.

        return http.build();
    }
}
