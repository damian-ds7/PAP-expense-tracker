package com.example.expenseapi.service;

import com.example.expenseapi.pojo.Currency;
import com.example.expenseapi.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl extends GenericServiceImpl<Currency, Long> implements CurrencyService {
    public CurrencyServiceImpl(CurrencyRepository repository) {
        super(repository);
    }
}