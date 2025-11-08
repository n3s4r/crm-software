package com.crm.project.crm_backend.repository;

import com.crm.project.crm_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * UserRepository: Handles database operations for the User entity.
 * Required for Spring Security to fetch user details during authentication.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User by their unique username.
     * @param username The login username.
     * @return An Optional containing the User if found.
     */
    Optional<User> findByUsername(String username);
}