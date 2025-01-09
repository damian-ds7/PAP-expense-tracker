package com.example.expenseapi.service;

import com.example.expenseapi.exception.BadRequestException;
import com.example.expenseapi.pojo.RefreshToken;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.RefreshTokenRepository;
import com.example.expenseapi.repository.UserRepository;
import com.example.expenseapi.utils.JwtUtil;
import org.springframework.stereotype.Service;

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
    public RefreshToken createAndSave(String email) {
        RefreshToken refreshToken = new RefreshToken();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty())
            throw new BadRequestException("Invalid email");
        refreshToken.setToken(jwtUtil.generateRefreshToken(email));
        refreshToken.setUser(user.get());
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
