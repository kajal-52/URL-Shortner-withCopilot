package com.spring.urlshortner.repository;

import com.spring.urlshortner.entity.ShortURL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortURL, Long> {

    Optional<ShortURL> findByShortCode(String shortCode);

    Optional<ShortURL> findByOriginalURL(String originalURL);
}
