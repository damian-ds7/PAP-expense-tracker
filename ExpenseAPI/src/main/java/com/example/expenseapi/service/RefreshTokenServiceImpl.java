package com.example.expenseapi.service;

import com.example.expenseapi.exception.EmailNotFound;
import com.example.expenseapi.exception.RefreshTokenNotFoundException;
import com.example.expenseapi.pojo.RefreshToken;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.RefreshTokenRepository;
import com.example.expenseapi.repository.UserRepository;
import com.example.expenseapi.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl extends GenericServiceImpl<RefreshToken, Long> implements RefreshTokenService  {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public RefreshTokenServiceImpl(RefreshTokenRepository repository, UserRepository userRepository, JwtUtil jwtUtil) {
        super(repository);
        this.refreshTokenRepository = repository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public RefreshToken findOrCreateToken(String email) {
        Optional<RefreshToken> optToken = refreshTokenRepository.findByUser_Email(email);
        return optToken.orElseGet(() -> createAndSave(email));
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException(token));
        return refreshToken.getExpiryDate().isBefore(Instant.now());
    }

    private RefreshToken createAndSave(String email) {
        RefreshToken refreshToken = new RefreshToken();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFound(email));
        refreshToken.setToken(jwtUtil.generateRefreshToken(email));
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(jwtUtil.getExpiration(refreshToken.getToken()).toInstant());
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    @Transactional
    public void deleteByUserId(Long Id) {
        refreshTokenRepository.deleteAllByUserId(Id);
    }
}
