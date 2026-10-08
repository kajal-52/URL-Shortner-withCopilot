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


@Controller
public class ShortURLController {

    private final URLShortnerService urlShortnerService;

    public ShortURLController(URLShortnerService urlShortnerService) {
        this.urlShortnerService = urlShortnerService;
    }

    @GetMapping("/urls")
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

    @GetMapping("/token")
    @ResponseBody
    public CsrfToken getToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
