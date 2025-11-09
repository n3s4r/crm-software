package com.crm.project.crm_backend.utility;

import com.crm.project.crm_backend.entity.Tenant;
import com.crm.project.crm_backend.entity.User;
import com.crm.project.crm_backend.repository.TenantRepository;
import com.crm.project.crm_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Component that runs on application startup to insert initial, secure mock data
 * for testing Multitenancy and User authentication.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring Boot automatically injects the dependencies (repositories and the password encoder)
    public DataInitializer(TenantRepository tenantRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Skip initialization if users already exist
        if (userRepository.count() > 0) {
            log.info("Database already populated. Skipping data initialization.");
            return;
        }

        log.info("Initializing mock tenant and user data...");

        // --- TENANT 1: Company A ---
        Tenant tenantA = new Tenant();
        UUID tenantAId = UUID.randomUUID();
        tenantA.setTenantId(tenantAId);
        tenantA.setName("Acme Solutions");
        tenantRepository.save(tenantA);

        User userA = new User();
        userA.setUsername("testuser@acme.com");
        // Password is "password" (hashed)
        userA.setPassword(passwordEncoder.encode("password"));
        userA.setTenantId(tenantAId);
        userA.setRole("ROLE_SALES");
        userRepository.save(userA);

        // --- TENANT 2: Company B ---
        Tenant tenantB = new Tenant();
        UUID tenantBId = UUID.randomUUID();
        tenantB.setTenantId(tenantBId);
        tenantB.setName("Beta Corp");
        tenantRepository.save(tenantB);

        User userB = new User();
        userB.setUsername("admin@beta.com");
        // Password is "password" (hashed)
        userB.setPassword(passwordEncoder.encode("password"));
        userB.setTenantId(tenantBId);
        userB.setRole("ROLE_ADMIN");
        userRepository.save(userB);

        log.info("Successfully initialized 2 tenants and 2 test users (password: 'password').");
        log.info("Test User 1 (Acme): testuser@acme.com | Tenant ID: {}", tenantAId);
        log.info("Test User 2 (Beta): admin@beta.com | Tenant ID: {}", tenantBId);
    }
}