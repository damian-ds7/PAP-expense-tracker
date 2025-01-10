package com.example.expenseapi.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${jwt.access.expiration.time}")
    private long ACCESS_EXPIRATION; // 1 hour

    @Value("${jwt.refresh.expiration.time}")
    private long REFRESH_EXPIRATION; // 7 days

    public String generateAccessToken(String email) {
        return buildToken(email, ACCESS_EXPIRATION);
    }

    public String generateRefreshToken(String email) {
        return buildToken(email, REFRESH_EXPIRATION);
    }

    private String buildToken(String email, long expiration) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String email) {
        return extractEmail(token).equals(email) && !isTokenExpired(token);
    }

    public LocalDate getExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    public boolean isTokenExpired(String token){
        return getExpiration(token).isBefore(LocalDate.now());
    }
}
