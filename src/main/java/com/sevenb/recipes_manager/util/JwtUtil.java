package com.sevenb.recipes_manager.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "my-secret-key-which-should-be-very-long-and-secure"; // igual que en tu JwtService

    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("id", Long.class);
    }

    public Claims extractClaims(String token) {
        return extractAllClaims(token);
    }
}
