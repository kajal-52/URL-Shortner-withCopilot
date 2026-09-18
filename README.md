# URL Shortener

A simple Spring Boot URL shortener that creates six-character links and redirects them to the original URL.

## AI-Assisted Development

This application was built from scratch with **GitHub Copilot** as the AI coding assistant. Copilot helped generate and refine the Spring Boot backend, JPA entity and repository, URL-shortening service, controller, validation, database configuration, and responsive Thymeleaf UI.

## Features

- Reuses an existing short link for the same URL
- Generates collision-safe short codes
- Uses H2 in-memory database with JPA
- Includes a responsive Thymeleaf web interface

## Requirements

- Java 17+
- Maven

## Run the application

```bash
./mvnw spring-boot:run
```

Open [http://localhost:8082](http://localhost:8082).

## Endpoints

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/` | Display the URL shortening form |
| `POST` | `/shorten` | Create or retrieve a short URL |
| `GET` | `/{shortCode}` | Redirect to the original URL |

The H2 database runs in memory, so saved links are cleared when the application stops.
