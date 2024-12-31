package com.example.expenseapi.web;
import com.example.expenseapi.dto.ExpenseFilter;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/expense")
@Tag(name="Expense Controller", description = "Controller to manage expense objects")
public class ExpenseController extends GenericController<Expense, Long> {
    public ExpenseController(ExpenseService service) {
        super(service);
    }

    @GetMapping("/my/expenses")
    @Operation(summary = "Retrieves expenses from logged-in user")
    @ApiResponse(responseCode = "200", description = "List of expense objects from logged user", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Expense.class))))
    public ResponseEntity<List<Expense>> getMyExpenses(@AuthenticationPrincipal UserDetails user) {
        ExpenseFilter filter = new ExpenseFilter();
        filter.setEmail(user.getUsername());
        return new ResponseEntity<>(((ExpenseService) service).searchExpenses(filter), HttpStatus.OK);
    }

    @GetMapping("/my/group/{name}")
    @Operation(summary = "Retrieves expenses for group of logged-in user")
    @ApiResponse(responseCode = "200", description = "List of expense object for group of logged-in user", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Expense.class))))
    public ResponseEntity<List<Expense>> getByGroup(@PathVariable String name) {
        return new ResponseEntity<>(((ExpenseService) service).getExpensesForGroup(name), HttpStatus.OK);
    }

    @GetMapping("/state/allGroups")
    @Operation(summary = "Returns sum of expenses for logged-in user and his group")
    @ApiResponse(responseCode = "200", description = "Sum of expenses for logged-in user and his group", content = @Content(schema = @Schema(implementation = ExpInfo.class)))
    public ResponseEntity<ExpInfo> getState() {
        return new ResponseEntity<>(((ExpenseService) service).getExpInfo(), HttpStatus.OK);
    }

    @GetMapping("/state/{group}")
    public ResponseEntity<ExpInfo> getState(@PathVariable String group) {
        return new ResponseEntity<>(((ExpenseService) service).getExpInfo(group), HttpStatus.OK);
    }

    @GetMapping("/dateMap/group/{name}")
    @Operation(summary = "Returns objects grouped by date for group of logged-in user")
    @ApiResponse(responseCode = "200", description = "All expenses grouped by date")
    public ResponseEntity<Map<LocalDate, List<Expense>>> getDateExpensesMap(@PathVariable String name) {
        return new ResponseEntity<>(((ExpenseService) service).getGroupExpenseAsDateMap(name), HttpStatus.OK);
    }

    @GetMapping("/categoryMap/group/{name}")
    @Operation(summary = "Returns objects grouped by category for group of logged-in user")
    @ApiResponse(responseCode = "200", description = "All expenses grouped by category")
    public ResponseEntity<Map<Category, List<Expense>>> getCategoryExpenseMap(@PathVariable String name) {
        return new ResponseEntity<>(((ExpenseService) service).getGroupExpenseAsCategoryMap(name), HttpStatus.OK);
    }

    @GetMapping("/recent")
    @Operation(summary = "Returns recent Expense object")
    @ApiResponse(responseCode = "200", description = "The most recent Expense object", content = @Content(schema = @Schema(implementation = Expense.class)))
    public ResponseEntity<Optional<Expense>> getRecent() {
        return new ResponseEntity<>(((ExpenseService) service).getRecentExpense(), HttpStatus.OK);
    }
}