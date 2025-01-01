package com.example.expenseapi.repository;

import com.example.expenseapi.pojo.MethodOfPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MethodOfPaymentRepository extends JpaRepository<MethodOfPayment, Long> {
    MethodOfPayment findByName(String methodOfPayment);
}
