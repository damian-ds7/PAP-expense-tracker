package com.example.expenseapi.repository;

import com.example.expenseapi.pojo.RefreshToken;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(@NonNull String token);
    Optional<RefreshToken> findByUser_Email(String userEmail);
    void deleteAllByUserId(Long id);

}
