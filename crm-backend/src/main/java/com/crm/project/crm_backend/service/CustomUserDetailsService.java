package com.crm.project.crm_backend.service;

import com.crm.project.crm_backend.entity.User;
import com.crm.project.crm_backend.multitenancy.TenantContext;
import com.crm.project.crm_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

/**
 * CustomUserDetailsService: Links user authentication to multitenancy.
 * Implements UserDetailsService to load user details for Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * This method is called by Spring Security during the login attempt.
     * @param username The username submitted by the user.
     * @return UserDetails object containing credentials and authorities.
     * @throws UsernameNotFoundException if the user does not exist.
     */
    @Override
    @Transactional(readOnly = true) // Read-only operation on User table
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Fetch the User from the database (User table is NOT tenant-filtered)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 2. CRUCIAL STEP for Multitenancy: Set the tenant ID for the current thread
        // This is necessary because the user will immediately perform tenant-specific actions after logging in.
        TenantContext.setTenantId(user.getTenantId());

        // 3. Prepare Authorities (Roles)
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        // 4. Return the Spring Security UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
