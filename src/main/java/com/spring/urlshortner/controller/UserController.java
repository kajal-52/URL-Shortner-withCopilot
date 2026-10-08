package com.spring.urlshortner.controller;


import com.spring.urlshortner.model.User;
import com.spring.urlshortner.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        var User = userRepository.findUserByUsername(user.getUsername());
        if(!Objects.isNull(User))
            return "Success";
        return "failure";
    }
}
