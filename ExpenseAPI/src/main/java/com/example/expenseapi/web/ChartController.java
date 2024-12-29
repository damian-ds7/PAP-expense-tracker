package com.example.expenseapi.web;

import com.example.expenseapi.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chart")
public class ChartController {
    private final ExpenseService service;

    public ChartController(ExpenseService service) {
        this.service = service;
    }

    @GetMapping("/my/group/{year}")
    @Operation(summary = "Retrieves map <Month, ExpensesSum> for given year")
    public ResponseEntity<Map<String, Double>> getUserMonthlyExpensesMap(@PathVariable String year, @RequestParam(defaultValue = "PLN") String currCode) {
        return new ResponseEntity<>(service.getMonthlyExpensesForGroup(year, currCode), HttpStatus.OK);
    }

    @GetMapping("/my/expenses/{year}")
    @Operation(summary = "Retrieves map <Month, ExpensesSum> for given year")
    public ResponseEntity<Map<String, Double>> getGroupMonthlyExpensesMap(@PathVariable String year, @RequestParam(defaultValue = "PLN") String currCode) {
        return new ResponseEntity<>(service.getMonthlyExpensesForUser(year, currCode), HttpStatus.OK);
    }

    @GetMapping("/my/group/categories/{begin}/{end}")
    @Operation(summary = "Retrieves map <CategoryName, ExpensesSum> for given time period, currCode (currency code ISO4217) - param")
    public ResponseEntity<Map<String, Double>> getGroupCategoryExpensesMap(@PathVariable String begin, @PathVariable String end, @RequestParam(defaultValue = "PLN") String currCode) {
        return new ResponseEntity<>(service.getSumOfCategoryExpansesForGroup(begin, end, currCode), HttpStatus.OK);
    }

    @GetMapping("/my/expenses/categories/{begin}/{end}")
    @Operation(summary = "Retrieves map <CategoryName, ExpensesSum> for given time period, currCode (currency code ISO4217) - param")
    public ResponseEntity<Map<String, Double>> getUserCategoryExpensesMap(@PathVariable String begin, @PathVariable String end, @RequestParam(defaultValue = "PLN") String currCode) {
        return new ResponseEntity<>(service.getSumOfCategoryExpansesForUser(begin, end, currCode), HttpStatus.OK);
    }
}