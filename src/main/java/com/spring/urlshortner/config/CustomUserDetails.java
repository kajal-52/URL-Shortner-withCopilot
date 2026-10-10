package com.spring.urlshortner.config;

import com.spring.urlshortner.model.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Adapts the application's user model to Spring Security's user details contract.
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    /**
     * Creates a security representation backed by an application user.
     *
     * @param user user whose credentials and identity are exposed
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Returns the user's application role.
     *
     * @return a singleton authority granting the {@code USER} role
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    /**
     * Returns the stored password for authentication.
     *
     * @return the user's encoded password, or {@code null} when absent
     */
    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the user's login name.
     *
     * @return the username
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Uses Spring Security's default account-expiration policy.
     *
     * @return {@code true} because this adapter does not expire accounts
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Uses Spring Security's default account-lock policy.
     *
     * @return {@code true} because this adapter does not lock accounts
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Uses Spring Security's default credential-expiration policy.
     *
     * @return {@code true} because this adapter does not expire credentials
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Uses Spring Security's default enabled-account policy.
     *
     * @return {@code true} because this adapter does not disable accounts
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
