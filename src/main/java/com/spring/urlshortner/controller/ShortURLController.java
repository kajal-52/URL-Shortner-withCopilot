package com.spring.urlshortner.controller;

import com.spring.urlshortner.entity.ShortURL;
import com.spring.urlshortner.exception.MaxCollisionException;
import com.spring.urlshortner.model.ShortenURLRequest;
import com.spring.urlshortner.model.ShortenURLResponse;
import com.spring.urlshortner.service.URLShortnerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ShortURLController {

    private final URLShortnerService urlShortnerService;

    public ShortURLController(URLShortnerService urlShortnerService) {
        this.urlShortnerService = urlShortnerService;
    }

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("shortenURLRequest", new ShortenURLRequest());
        return "index";
    }

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

    private String getBaseUrl(String scheme, String host) {
        return scheme + "://" + host;
    }

    @GetMapping("/{shortCode}")
    public String redirectToOriginalURL(@PathVariable String shortCode) {
        String originalURL = urlShortnerService.getOriginalURL(shortCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Short URL not found"));
        return "redirect:" + originalURL;
    }
}
