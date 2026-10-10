package com.spring.urlshortner.service;

import com.spring.urlshortner.model.User;
import com.spring.urlshortner.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Registers users with encoded passwords and authenticates login credentials.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Creates the service with persistence, password, authentication, and token dependencies.
     *
     * @param userRepository stores newly registered users
     * @param passwordEncoder encodes passwords before persistence
     * @param authenticationManager verifies login credentials
     * @param jwtService creates a token after successful authentication
     */
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Encodes the supplied password and persists the user account.
     *
     * @param user account to register
     * @return the persisted user
     */
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Authenticates the supplied credentials and issues a JWT on success.
     *
     * @param user username and raw password to verify
     * @return a JWT if authentication succeeds, otherwise {@code "failure"}
     * @throws org.springframework.security.core.AuthenticationException if authentication fails
     */
    public String verify(User user) {
        Authentication authenticate= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//        var User = userRepository.findUserByUsername(user.getUsername());
//        if(!Objects.isNull(User))
        if(authenticate.isAuthenticated())
            return jwtService.generateJsonWebToken(user);
        return "failure";


    }
}
