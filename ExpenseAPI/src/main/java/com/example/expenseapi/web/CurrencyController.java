package com.example.expenseapi.web;

import com.example.expenseapi.service.CurrencyService;
import com.example.expenseapi.pojo.Currency;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/currency")
@Tag(name="Currency Controller", description = "Controller to handle currency operations")
public class CurrencyController extends GenericController<Currency, Long> {
    public CurrencyController(CurrencyService service) {
        super(service);
    }
}