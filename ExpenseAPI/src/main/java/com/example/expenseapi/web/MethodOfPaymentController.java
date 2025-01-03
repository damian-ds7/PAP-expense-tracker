package com.example.expenseapi.web;

import com.example.expenseapi.pojo.MethodOfPayment;
import com.example.expenseapi.service.MethodOfPaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/method")
@Tag(name = "Payment Method Controller", description = "Controller to manage payment methods")
public class MethodOfPaymentController extends GenericController<MethodOfPayment, Long> {
    public MethodOfPaymentController(MethodOfPaymentService service) {
        super(service);
    }
}
