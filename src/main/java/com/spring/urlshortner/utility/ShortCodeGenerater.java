package com.spring.urlshortner.utility;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ShortCodeGenerater {

    // URL-safe Base64 characters used for six-character short codes.
    private static final String BASE64_ALPHABET =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int CODE_LENGTH = 6;

    // SecureRandom provides unpredictable code values.
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a random six-character code for use as a shortened URL.
     *
     * @return a six-character alphanumeric short code
     */
    public String generateShortCode() {
        StringBuilder shortCode = new StringBuilder(CODE_LENGTH);
        for (int index = 0; index < CODE_LENGTH; index++) {
            // Select one random character for each position.
            int characterIndex = secureRandom.nextInt(BASE64_ALPHABET.length());
            shortCode.append(BASE64_ALPHABET.charAt(characterIndex));
        }
        return shortCode.toString();
    }
}
