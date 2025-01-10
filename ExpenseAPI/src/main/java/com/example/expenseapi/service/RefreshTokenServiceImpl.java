package com.example.expenseapi.service;

import com.example.expenseapi.exception.BadRequestException;
import com.example.expenseapi.pojo.RefreshToken;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.RefreshTokenRepository;
import com.example.expenseapi.repository.UserRepository;
import com.example.expenseapi.utils.JwtUtil;
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
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isPresent()) {
            return refreshToken.get().getExpiryDate().isBefore(Instant.now());
        }
        else throw new BadRequestException("Token does not exist");
    }

    private RefreshToken createAndSave(String email) {
        RefreshToken refreshToken = new RefreshToken();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty())
            throw new BadRequestException("Invalid email");
        refreshToken.setToken(jwtUtil.generateRefreshToken(email));
        refreshToken.setUser(user.get());
        refreshToken.setExpiryDate(jwtUtil.getExpiration(refreshToken.getToken()).toInstant());
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
}
