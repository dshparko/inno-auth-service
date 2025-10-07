package com.innowise.authservice.repository;

import com.innowise.authservice.model.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @ClassName CredetialRepository
 * @Description Repository interface for accessing {@link Credential} entities.
 * @Author dshparko
 * @Date 07.10.2025 15:11
 * @Version 1.0
 */
public interface CredentialRepository extends JpaRepository<Credential, Long> {
    /**
     * Finds a credential by its associated email.
     *
     * @param email the email to search for
     * @return an optional containing the credential if found
     */
    Optional<Credential> findByEmail(String email);

    /**
     * Checks whether a credential exists for the given email.
     *
     * @param email the email to check
     * @return true if a credential exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Saves a credential entity to the database.
     *
     * @param credential the credential entity to persist
     * @return the saved credential with generated ID (if new)
     */
    Credential save(Credential credential);
}
