package com.spring.urlshortner.service;

import com.spring.urlshortner.config.CustomUserDetails;
import com.spring.urlshortner.model.User;
import com.spring.urlshortner.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Loads registered users from the repository for Spring Security authentication.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Creates the service with the user repository used for account lookup.
     *
     * @param userRepository repository containing registered users
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a registered user and adapts it to Spring Security's user contract.
     *
     * @param username account name to load
     * @return security details for the matching account
     * @throws UsernameNotFoundException if no account has the supplied username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =userRepository.findUserByUsername(username);
        if(Objects.isNull(user)){
            System.out.println("user not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }
}
