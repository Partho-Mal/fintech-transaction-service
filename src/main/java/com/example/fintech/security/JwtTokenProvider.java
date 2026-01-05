package com.example.fintech.security;

import com.example.fintech.domain.Role; // Make sure this Enum exists
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtTokenProvider {

    private final Key key;

    // Inject the secret from application.yml (which gets it from Docker env vars)
    public JwtTokenProvider(@Value("${app.jwt.secret}") String jwtSecret) {
        // Ensure the key is at least 32 chars (256 bits)
        // Spring will inject the long secure key we defined in docker-compose
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            // Log this in production: log.error("Invalid JWT: {}", ex.getMessage());
            return false;
        }
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Role getRole(String token) {
        String role = getClaims(token).get("role", String.class);
        return Role.valueOf(role);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}