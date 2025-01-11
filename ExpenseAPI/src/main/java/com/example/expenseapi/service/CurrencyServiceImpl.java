package com.example.expenseapi.service;

import com.example.expenseapi.pojo.Currency;
import com.example.expenseapi.repository.CurrencyRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl extends GenericServiceImpl<Currency, Long> implements CurrencyService {
    public CurrencyServiceImpl(CurrencyRepository repository) {
        super(repository);
    }

    @Override
    @Cacheable(value = "currencies", key = "'all'")
    public List<Currency> getAll() {
        return super.getAll();
    }
}