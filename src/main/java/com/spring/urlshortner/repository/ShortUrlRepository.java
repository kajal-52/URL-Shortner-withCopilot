package com.spring.urlshortner.repository;

import com.spring.urlshortner.entity.ShortURL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Provides persistence and lookup operations for stored short URLs.
 */
public interface ShortUrlRepository extends JpaRepository<ShortURL, Long> {

    /**
     * Finds a persisted short URL by its generated code.
     *
     * @param shortCode code to look up
     * @return the matching record, if present
     */
    Optional<ShortURL> findByShortCode(String shortCode);

    /**
     * Finds a persisted short URL by its original address.
     *
     * @param originalURL original address to look up
     * @return the matching record, if present
     */
    Optional<ShortURL> findByOriginalURL(String originalURL);
}
