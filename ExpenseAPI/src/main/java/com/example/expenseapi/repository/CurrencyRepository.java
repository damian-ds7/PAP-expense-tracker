package com.example.expenseapi.repository;

import com.example.expenseapi.pojo.Currency;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findBySymbol(@NonNull String symbol);
}
