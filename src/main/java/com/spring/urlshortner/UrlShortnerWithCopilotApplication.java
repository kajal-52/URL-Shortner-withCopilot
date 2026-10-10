package com.spring.urlshortner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Bootstraps the Spring application for the URL shortener.
 */
@SpringBootApplication
public class UrlShortnerWithCopilotApplication {

    /**
     * Starts the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(UrlShortnerWithCopilotApplication.class, args);
    }

}
