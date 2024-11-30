package com.example.expenseapi.web;
import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.service.ExpenseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expense")
public class ExpenseController extends GenericController<Expense, Long> {
    public ExpenseController(ExpenseService service) {
        super(service);
    }
}