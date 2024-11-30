package com.example.expenseapi.web;

import com.example.expenseapi.pojo.ExpInfo;
import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.pojo.ExpenseNotFound;
import com.example.expenseapi.service.ExpenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/expense")
public class ExpenseController {
    ExpenseService expenseService;

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpense(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(expenseService.getExpense(id), HttpStatus.OK);
        } catch (ExpenseNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Expense> saveExpense(@RequestBody Expense expense) {
        return new ResponseEntity<>(expenseService.saveExpense(expense), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Expense> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Expense>> getExpenses() {
        return new ResponseEntity<>(expenseService.getAllExpenses(), HttpStatus.OK);
    }

    @GetMapping("/initial/{email}")
    public ResponseEntity<ExpInfo> getExpenseByEmail(@PathVariable String email) {
        List<Expense>personalExpenses = expenseService.getExpensesByEmail(email);
        double sumOfUserExpenses = personalExpenses.stream()
                .mapToDouble(Expense::getPrice)
                .sum();
        List<Expense>allExpenses = expenseService.getAllExpenses();
        double sumOfAllExpenses = allExpenses.stream()
                .mapToDouble(Expense::getPrice)
                .sum();
        return new ResponseEntity<>(new ExpInfo(sumOfUserExpenses, sumOfAllExpenses), HttpStatus.OK);
    }

}
