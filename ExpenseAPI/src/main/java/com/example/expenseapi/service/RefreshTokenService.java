package com.example.expenseapi.service;

import com.example.expenseapi.pojo.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService extends GenericService<RefreshToken, Long> {
    RefreshToken createAndSave(String email);
    Optional<RefreshToken> findByToken(String token);
}
