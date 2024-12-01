package com.example.expenseapi.web;
import com.example.expenseapi.pojo.Category;
import com.example.expenseapi.pojo.ExpInfo;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/group/{name}")
    @Operation(summary = "Retrieves expenses for given user group")
    @ApiResponse(responseCode = "200", description = "List of expense object for given user group", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Expense.class))))
    public ResponseEntity<List<Expense>> getByGroup(@PathVariable String name) {
        return new ResponseEntity<>(((ExpenseService) service).getExpensesForGroup(name), HttpStatus.OK);
    }

    @GetMapping("/state/group/{name}/user/{userId}")
    @Operation(summary = "Returns sum of expenses for given group and user")
    @ApiResponse(responseCode = "200", description = "Sum of expenses for given group and user", content = @Content(schema = @Schema(implementation = ExpInfo.class)))
    public ResponseEntity<ExpInfo> getState(@PathVariable String name, @PathVariable String userId) {
        return new ResponseEntity<>(((ExpenseService) service).getExpInfo(name, userId), HttpStatus.OK);
    }

    @GetMapping("/all/dateMap")
    @Operation(summary = "Returns all objects grouped by date")
    @ApiResponse(responseCode = "200", description = "All expenses grouped by date")
    public ResponseEntity<Map<LocalDate, List<Expense>>> getDateExpensesMap() {
        return new ResponseEntity<>(((ExpenseService) service).getDateExpenseAsMap(), HttpStatus.OK);
    }

    @GetMapping("/all/categoryMap")
    @Operation(summary = "Returns all objects grouped by category")
    @ApiResponse(responseCode = "200", description = "All expenses grouped by category")
    public ResponseEntity<Map<Category, List<Expense>>> getCategoryExpenseMap() {
        return new ResponseEntity<>(((ExpenseService) service).getCategoryExpenseAsMap(), HttpStatus.OK);
    }

}