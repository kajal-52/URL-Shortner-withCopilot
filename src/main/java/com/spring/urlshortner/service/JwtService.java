package com.spring.urlshortner.service;

import com.spring.urlshortner.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Creates and validates signed JWTs for application users.
 */
@Service
public class JwtService {

    private String secretKey = null;

    /**
     * Creates a signed token for the supplied user, valid for ten minutes.
     *
     * @param user user whose username becomes the token subject
     * @return compact signed JWT
     */
    public String generateJsonWebToken(User user) {
        Map<String, Object> claims
                = new HashMap<>();
        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuer("DCB")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 60*10*1000))
                .and()
                .signWith(generateKey())
                .compact();
    }

    /**
     * Decodes the configured Base64 secret into an HMAC signing key.
     *
     * @return signing key used to sign and verify tokens
     */
    private SecretKey generateKey() {
        byte[] decode
                = Decoders.BASE64.decode(getSecretKey());

        return Keys.hmacShaKeyFor(decode);
    }

    /**
     * Returns the Base64-encoded signing secret.
     *
     * @return configured Base64 secret
     */
    public String getSecretKey() {
        return secretKey = "RqxPOuVfHoBA8Uq40MhJvfY6qEHOOWWvg6N9W9vt23s=";
    }

    /**
     * Extracts the username from a signed token.
     *
     * @param token signed JWT to inspect
     * @return token subject
     * @throws io.jsonwebtoken.JwtException if the token is invalid or cannot be parsed
     */
    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Resolves a claim from the token's verified claim set.
     *
     * @param token signed JWT to inspect
     * @param claimResolver function selecting a claim value
     * @param <T> resolved claim type
     * @return the selected claim value
     * @throws io.jsonwebtoken.JwtException if the token is invalid or cannot be parsed
     */
    private <T> T extractClaims(String token, Function<Claims,T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    /**
     * Parses the token and verifies its signature before returning its claims.
     *
     * @param token signed JWT to parse
     * @return verified token claims
     * @throws io.jsonwebtoken.JwtException if the token is invalid or cannot be parsed
     */
    private Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Checks that the token subject matches the user and that the token has not expired.
     *
     * @param token signed JWT to validate
     * @param userDetails expected authenticated user
     * @return {@code true} if the subject matches and the token is unexpired
     * @throws io.jsonwebtoken.JwtException if the token is invalid or cannot be parsed
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Determines whether the token's expiration time has passed.
     *
     * @param token signed JWT to inspect
     * @return {@code true} if the token is expired
     * @throws io.jsonwebtoken.JwtException if the token is invalid or cannot be parsed
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration claim from the verified token.
     *
     * @param token signed JWT to inspect
     * @return token expiration time
     * @throws io.jsonwebtoken.JwtException if the token is invalid or cannot be parsed
     */
    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}