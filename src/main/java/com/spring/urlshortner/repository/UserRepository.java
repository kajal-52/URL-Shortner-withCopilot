package com.spring.urlshortner.repository;

import com.spring.urlshortner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides persistence and username lookup operations for users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a registered user by username.
     *
     * @param username account name to look up
     * @return the matching user, or {@code null} when no user is found
     */
    User findUserByUsername(String username);
}
