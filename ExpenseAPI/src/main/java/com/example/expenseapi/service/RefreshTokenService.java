package com.example.expenseapi.service;

import com.example.expenseapi.pojo.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService extends GenericService<RefreshToken, Long> {
    RefreshToken findOrCreateToken(String email);
    Optional<RefreshToken> findByToken(String token);
    boolean isTokenExpired(String token);
}
