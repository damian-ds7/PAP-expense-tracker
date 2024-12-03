package com.example.expenseapi.web;

import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/filter")
@Tag(name="Filter Controller", description = "Controller to filter expenses")
public class FilterController {
    private final ExpenseService service;
    public FilterController(ExpenseService service) {
        this.service = service;
    }
    @GetMapping("/category/{name}")
    @Operation(summary = "Retrieves expenses with given category")
    @ApiResponse(responseCode = "200", description = "List of expense objects with given category", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Expense.class))))
    public ResponseEntity<List<Expense>> getByCategory(@PathVariable String name) {
        return new ResponseEntity<>(((service).getExpensesByCategory(name)), HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Retrieves expenses with given date")
    @ApiResponse(responseCode = "200", description = "List of expense objects with given date", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Expense.class))))
    public ResponseEntity<List<Expense>> getByDate(@PathVariable String date) {
        return new ResponseEntity<>((service).getExpensesByDate(date), HttpStatus.OK);
    }

    @GetMapping("/date/{begin}/{end}")
    @Operation(summary = "Retrieves expenses between given dates")
    @ApiResponse(responseCode = "200", description = "List of expense objects between given dates", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Expense.class))))
    public ResponseEntity<List<Expense>> getByPeriod(@PathVariable String begin, @PathVariable String end) {
        return new ResponseEntity<>((service).getExpensesByPeriod(begin, end), HttpStatus.OK);
    }

    @GetMapping("/price/{begin}/{end}")
    @Operation(summary = "Retrieves expenses between given prices")
    @ApiResponse(responseCode = "200", description = "List of expense object between given prices", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Expense.class))))
    public ResponseEntity<List<Expense>> getByPriceRange(@PathVariable double begin, @PathVariable double end) {
        return new ResponseEntity<>((service).getExpensesWherePriceInRange(begin, end), HttpStatus.OK);
    }

    @GetMapping("/price/lower/{price}")
    @Operation(summary = "Retrieves expenses lower than given price")
    @ApiResponse(responseCode = "200", description = "List of expense object which price is lower than given", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Expense.class))))
    public ResponseEntity<List<Expense>> getByPriceLowerThan(@PathVariable double price) {
        return new ResponseEntity<>((service).getExpensesWherePriceIsLower(price), HttpStatus.OK);
    }

    @GetMapping("/price/greater/{price}")
    @Operation(summary = "Retrieves expenses greater than given price")
    @ApiResponse(responseCode = "200", description = "List of expense object which price is greater than given", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Expense.class))))
    public ResponseEntity<List<Expense>> getByPriceGreaterThan(@PathVariable double price) {
        return new ResponseEntity<>((service).getExpensesWherePriceIsGreater(price), HttpStatus.OK);
    }
}
