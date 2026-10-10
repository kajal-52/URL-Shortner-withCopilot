package com.spring.urlshortner.controller;


import com.spring.urlshortner.model.User;
import com.spring.urlshortner.repository.UserRepository;
import com.spring.urlshortner.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Exposes the user registration and login endpoints.
 */
@RestController
public class UserController {


    private final UserService userService;

    /**
     * Creates the controller with the user registration and authentication service.
     *
     * @param userService handles registration and login verification
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a user account.
     *
     * @param user account details to register
     * @return the persisted user
     */
    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
        return userService.register(user);
    }

    /**
     * Verifies a user's login credentials.
     *
     * @param user credentials to verify
     * @return a JWT on success, or the service's failure result
     */
    @PostMapping("/login")
    public String login(@RequestBody User user){
        return userService.verify(user);}
}
