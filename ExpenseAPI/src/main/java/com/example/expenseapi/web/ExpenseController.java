package com.example.expenseapi.web;
import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.pojo.ExpenseNotFound;
import com.example.expenseapi.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController extends GenericController<Expense, Long> {
    public ExpenseController(ExpenseService service) {
        super(service);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<Expense>> getByEmail(@PathVariable String email) {
        return new ResponseEntity<>(((ExpenseService) service).getExpensesByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<List<Expense>> getByCategory(@PathVariable String name) {
        return new ResponseEntity<>(((ExpenseService) service).getExpensesByCategory(name), HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Expense>> getByDate(@PathVariable String date) {
        return new ResponseEntity<>(((ExpenseService) service).getExpensesByDate(date), HttpStatus.OK);
    }

    @GetMapping("/date/{begin}/{end}")
    public ResponseEntity<List<Expense>> getByPeriod(@PathVariable String begin, @PathVariable String end) {
        return new ResponseEntity<>(((ExpenseService) service).getExpensesByPeriod(begin, end), HttpStatus.OK);
    }
}