//JwtTokenProvider
//validate token
//extract username
//extract role

package com.example.fintech.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtTokenProvider {

    private final Key key =
            Keys.hmacShaKeyFor("very-secret-key-very-secret-key".getBytes());

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Role getRole(String token) {
        String role = (String) getClaims(token).get("role");
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
