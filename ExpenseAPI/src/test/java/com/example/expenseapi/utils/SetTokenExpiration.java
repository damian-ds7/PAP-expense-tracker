package com.example.expenseapi.utils;

import com.example.expenseapi.pojo.RefreshToken;
import com.example.expenseapi.repository.RefreshTokenRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.time.LocalDate;


@Configuration
@Profile("test")
public class SetTokenExpiration {

    @Bean
    public CommandLineRunner setTokenExpirationDates(RefreshTokenRepository refreshTokenRepository) {
    return args -> {
        RefreshToken token1 = refreshTokenRepository.findByToken("token1").get();
        RefreshToken token2 = refreshTokenRepository.findByToken("token2").get();
        token1.setExpiryDate(Instant.now().plusSeconds(3600));
        token2.setExpiryDate(Instant.now().minusSeconds(3600));
        refreshTokenRepository.save(token1);
        refreshTokenRepository.save(token2);
        };
    }
}
