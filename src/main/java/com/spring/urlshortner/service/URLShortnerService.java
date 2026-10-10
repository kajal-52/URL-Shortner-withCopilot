package com.spring.urlshortner.service;

import com.spring.urlshortner.entity.ShortURL;
import com.spring.urlshortner.exception.MaxCollisionException;
import com.spring.urlshortner.repository.ShortUrlRepository;
import com.spring.urlshortner.utility.ShortCodeGenerater;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

/**
 * Creates short URL records with unique codes and resolves codes to original URLs.
 */
@Service
@Transactional
public class URLShortnerService {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortCodeGenerater shortCodeGenerater;
    private static final int MAX_COLLISIONS = 10;


    /**
     * Creates the service with URL persistence and short-code generation dependencies.
     *
     * @param shortUrlRepository repository for shortened URL records
     * @param shortCodeGenerater generator for candidate codes
     */
    public URLShortnerService(ShortUrlRepository shortUrlRepository, ShortCodeGenerater shortCodeGenerater) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortCodeGenerater = shortCodeGenerater;
    }

    /**
     * Returns an existing short URL or creates one for the supplied original URL.
     *
     * @param originalURL the URL to shorten
     * @return the existing or newly saved short URL
     * @throws MaxCollisionException if a unique code cannot be generated
     */
    public ShortURL createShortURL(String originalURL) throws MaxCollisionException {
        Optional<ShortURL> existingURL = shortUrlRepository.findByOriginalURL(originalURL);
        if (existingURL.isPresent()) {
            return existingURL.get();
        }
        String shortCode = generateUniqueShortCode();
        ShortURL shortURL = new ShortURL();
        shortURL.setOriginalURL(originalURL);
        shortURL.setShortCode(generateUniqueShortCode());
        return shortUrlRepository.save(shortURL);
    }

    /**
     * Generates a short code and retries when the code is already in use.
     *
     * @return a unique six-character short code
     * @throws MaxCollisionException if repeated generated codes cannot be made unique
     */
    private String generateUniqueShortCode() throws MaxCollisionException {
        int attempts = 0;
        String shortCode;
        do {
            shortCode = shortCodeGenerater.generateShortCode();
            attempts++;
            if (attempts > MAX_COLLISIONS) {
                throw new MaxCollisionException("Failed to generate short code"+MAX_COLLISIONS+" attempts");
            }
        } while (shortUrlRepository.findByShortCode(shortCode).isPresent());

        return shortCode;
    }

    /**
     * Looks up the original URL associated with a short code.
     *
     * @param shortCode the short code to resolve
     * @return the original URL when the short code exists
     */
    @Transactional(readOnly = true)
    public Optional<String> getOriginalURL(String shortCode) {
        return shortUrlRepository.findByShortCode(shortCode)
                .map(ShortURL::getOriginalURL);
    }
}
