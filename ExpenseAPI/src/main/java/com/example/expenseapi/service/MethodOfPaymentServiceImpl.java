package com.example.expenseapi.service;

import com.example.expenseapi.pojo.MethodOfPayment;
import com.example.expenseapi.repository.MethodOfPaymentRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MethodOfPaymentServiceImpl extends GenericServiceImpl<MethodOfPayment, Long> implements MethodOfPaymentService {
    public MethodOfPaymentServiceImpl(MethodOfPaymentRepository repository) {super(repository);}

    @Override
    @Cacheable(value = "methods", key = "'all'")
    public List<MethodOfPayment> getAll() {
        return super.getAll();
    }
}
