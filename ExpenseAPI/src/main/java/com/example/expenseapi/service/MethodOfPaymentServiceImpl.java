package com.example.expenseapi.service;

import com.example.expenseapi.pojo.MethodOfPayment;
import com.example.expenseapi.repository.MethodOfPaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class MethodOfPaymentServiceImpl extends GenericServiceImpl<MethodOfPayment, Long> implements MethodOfPaymentService {
    public MethodOfPaymentServiceImpl(MethodOfPaymentRepository repository) {super(repository);}
}
