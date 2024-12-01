package com.example.expenseapi.web;
import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.pojo.ExpenseNotFound;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/expense")
@Tag(name="Expense Controller", description = "Controller to manage expense objects")
public class ExpenseController extends GenericController<Expense, Long> {
    public ExpenseController(ExpenseService service) {
        super(service);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Retrieves expenses from user with given email")
    @ApiResponse(responseCode = "200", description = "List of expense objects from the user with given email", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Expense.class))))
    public ResponseEntity<List<Expense>> getByEmail(@PathVariable String email) {
        return new ResponseEntity<>(((ExpenseService) service).getExpensesByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/category/{name}")
    @Operation(summary = "Retrieves expenses with given category")
    @ApiResponse(responseCode = "200", description = "List of expense objects with given category", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Expense.class))))
    public ResponseEntity<List<Expense>> getByCategory(@PathVariable String name) {
        return new ResponseEntity<>(((ExpenseService) service).getExpensesByCategory(name), HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Retrieves expenses with given date")
    @ApiResponse(responseCode = "200", description = "List of expense objects with given date", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Expense.class))))
    public ResponseEntity<List<Expense>> getByDate(@PathVariable String date) {
        return new ResponseEntity<>(((ExpenseService) service).getExpensesByDate(date), HttpStatus.OK);
    }

    @GetMapping("/date/{begin}/{end}")
    @Operation(summary = "Retrieves expenses between given dates")
    @ApiResponse(responseCode = "200", description = "List of expense objects between given dates", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Expense.class))))
    public ResponseEntity<List<Expense>> getByPeriod(@PathVariable String begin, @PathVariable String end) {
        return new ResponseEntity<>(((ExpenseService) service).getExpensesByPeriod(begin, end), HttpStatus.OK);
    }
}