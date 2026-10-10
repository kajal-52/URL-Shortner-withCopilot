package com.spring.urlshortner.controller;


import com.spring.urlshortner.model.User;
import com.spring.urlshortner.repository.UserRepository;
import com.spring.urlshortner.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        return userService.verify(user);}
}
