package com.example.expenseapi.repository;

import com.example.expenseapi.pojo.Currency;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {}
