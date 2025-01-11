package com.example.expenseapi.repository;

import com.example.expenseapi.pojo.TemporaryMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemporaryMembershipRepository extends JpaRepository<TemporaryMembership, Long> {
    List<TemporaryMembership> findByUserId(Long userId);
    List<TemporaryMembership> findBySenderId(Long senderId);
}
