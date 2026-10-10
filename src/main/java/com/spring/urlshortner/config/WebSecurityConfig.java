package com.spring.urlshortner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures the application's HTTP security, authentication provider, and password encoder.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Creates the security configuration with its user lookup and JWT filter.
     *
     * @param userDetailsService loads users for authentication
     * @param jwtAuthenticationFilter validates bearer tokens
     */
    public WebSecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configures request authorization and the application's HTTP security filters.
     *
     * @param httpSecurity security builder configured for this application
     * @return the built security filter chain
     * @throws Exception if the security chain cannot be built
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/register","/login").permitAll()
                                .anyRequest().authenticated()
                )
//                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    //@Bean
    /**
     * Creates an in-memory user directory for the sample accounts.
     *
     * @return a service containing the configured sample users
     */
    public UserDetailsService userDetailsService() {
        UserDetails shabbir
                = User.withUsername("shabbir")
                .password("{noop}password")
                .roles("USER")
                .build();

        UserDetails nikhil
                = User.withUsername("nikhil")
                .password("{noop}nikhil")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(shabbir,nikhil);
    }

    /**
     * Creates the password encoder used for stored credentials.
     *
     * @return a BCrypt encoder configured with strength 14
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(14);
    }

    /**
     * Creates the DAO provider that authenticates users from the configured service.
     *
     * @return an authentication provider using BCrypt password verification
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider
                = new DaoAuthenticationProvider(userDetailsService);
        //provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    /**
     * Obtains Spring Security's configured authentication manager.
     *
     * @param configuration authentication configuration supplied by Spring Security
     * @return the configured authentication manager
     * @throws Exception if the manager cannot be obtained
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }
}