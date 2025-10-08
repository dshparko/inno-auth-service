package com.innowise.authservice.repository;

import com.innowise.authservice.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @ClassName RoleRepository
 * @Description Repository interface for accessing {@link Role} entities.
 * @Author dshparko
 * @Date 05.10.2025 15:51
 * @Version 1.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by its name (e.g., "USER", "ADMIN").
     *
     * @param roleName the name of the role
     * @return an optional containing the role if found
     */
    Optional<Role> findByName(String roleName);
}
