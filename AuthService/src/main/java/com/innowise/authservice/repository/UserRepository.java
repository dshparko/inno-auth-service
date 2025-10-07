package com.innowise.authservice.repository;

import com.innowise.authservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @ClassName UserRepository
 * @Description Repository interface for accessing {@link User} entities.
 * @Author dshparko
 * @Date 04.10.2025 19:19
 * @Version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Saves a user entity to the database.
     *
     * @param user the user entity to persist
     * @return the saved user with generated ID (if new)
     */
    User save(User user);
}
