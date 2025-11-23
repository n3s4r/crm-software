package com.crm.project.crm_backend.repository;

import com.crm.project.crm_backend.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * TenantRepository: Handles database operations for the Tenant entity.
 * UUID is used as the primary key type.
 */
public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    // Basic CRUD methods (save, findById, findAll, etc.) are inherited from JpaRepository.
    // No custom methods are needed here yet.
}