package com.example.expenseapi.web;

import com.example.expenseapi.dto.ExpenseFilter;
import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.service.ExpenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/filter")
@Tag(name="Filter Controller", description = "Controller to filter expenses")
public class FilterController {
    private final ExpenseService service;
    public FilterController(ExpenseService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Expense>> search(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String beginDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) List<String> emails
    ) {
        ExpenseFilter filter = new ExpenseFilter();
        filter.setCategoryName(category);
        filter.setGroupName(groupName);
        filter.setPriceMin(minPrice);
        filter.setPriceMax(maxPrice);
        filter.setEmail(email);
        if (groupName != null) filter.setEmails(emails);
        if (beginDate != null) filter.setBeginDate(LocalDate.parse(beginDate));
        if (endDate != null) filter.setEndDate(LocalDate.parse(endDate));

        return new ResponseEntity<>(service.searchExpenses(filter), HttpStatus.OK);
    }
}
