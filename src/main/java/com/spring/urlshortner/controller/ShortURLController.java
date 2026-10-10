package com.spring.urlshortner.controller;

import com.spring.urlshortner.entity.ShortURL;
import com.spring.urlshortner.exception.MaxCollisionException;
import com.spring.urlshortner.model.ShortenURLRequest;
import com.spring.urlshortner.model.ShortenURLResponse;
import com.spring.urlshortner.service.URLShortnerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


/**
 * Handles web requests for creating short URLs, resolving redirects, and returning CSRF tokens.
 */
@Controller
public class ShortURLController {

    private final URLShortnerService urlShortnerService;

    /**
     * Creates the controller with the URL-shortening service.
     *
     * @param urlShortnerService creates and resolves short URLs
     */
    public ShortURLController(URLShortnerService urlShortnerService) {
        this.urlShortnerService = urlShortnerService;
    }

    /**
     * Displays the form used to submit a URL for shortening.
     *
     * @param model view model populated with an empty request object
     * @return the form view name
     */
    @GetMapping("/urls")
    public String showForm(Model model) {
        model.addAttribute("shortenURLRequest", new ShortenURLRequest());
        return "index";
    }

    /**
     * Validates a submitted URL and adds its generated short URL to the form model.
     *
     * @param request submitted URL data
     * @param bindingResult validation result for the submitted data
     * @param host request host used to construct the short URL
     * @param scheme forwarded request scheme used to construct the short URL
     * @param model view model receiving the shortening result
     * @return the form view name
     * @throws MaxCollisionException if a unique short code cannot be generated
     */
    @PostMapping("/shorten")
    public String shortenURL(
            @Valid @ModelAttribute("shortenURLRequest") ShortenURLRequest request,
            BindingResult bindingResult,
            @RequestHeader(HttpHeaders.HOST) String host,
            @RequestHeader(value = "X-Forwarded-Proto", defaultValue = "http") String scheme,
            Model model) throws MaxCollisionException {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        ShortURL shortURL = urlShortnerService.createShortURL(request.getUrl());
        String baseUrl = getBaseUrl(scheme, host);
        String shortURLValue = baseUrl + "/" + shortURL.getShortCode();
        model.addAttribute("shortenURLResponse", new ShortenURLResponse(
                shortURL.getShortCode(),
                shortURLValue,
                shortURL.getOriginalURL(),
                shortURL.getCreatedAt(),
                shortURL.getExpiresAt()));
        return "index";
    }

    /**
     * Combines the request scheme and host into an origin URL.
     *
     * @param scheme URL scheme
     * @param host request host
     * @return the scheme and host joined as a base URL
     */
    private String getBaseUrl(String scheme, String host) {
        return scheme + "://" + host;
    }

    /**
     * Resolves a short code and redirects to its original URL.
     *
     * @param shortCode code from the requested path
     * @return redirect view targeting the original URL
     * @throws ResponseStatusException with HTTP 404 when no matching URL exists
     */
    @GetMapping("/{shortCode}")
    public String redirectToOriginalURL(@PathVariable String shortCode) {
        String originalURL = urlShortnerService.getOriginalURL(shortCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Short URL not found"));
        return "redirect:" + originalURL;
    }

    /**
     * Returns the CSRF token attached to the current request.
     *
     * @param request current HTTP request
     * @return the request's CSRF token, or {@code null} if it is unavailable
     */
    @GetMapping("/token")
    @ResponseBody
    public CsrfToken getToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
