package com.example.expenseapi.web;

import com.example.expenseapi.filter.ExpenseFilter;
import com.example.expenseapi.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chart")
@Tag(name="Chart Controller", description = "Controller to handle chart generation")
public class ChartController {
    private final ExpenseService service;

    public ChartController(ExpenseService service) {
        this.service = service;
    }

    @GetMapping("/map-result/{group}/{keyPattern}")
    @Operation(summary = "Retrieves a map<String, TotalExpanses> based on the given filter")
    public ResponseEntity<Map<String, Double>> getMapResult(
            @RequestParam(required = false) String beginDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) List<String> categoryNames,
            @RequestParam(required = false) List<String> emails,
            @RequestParam(required = false) List<String> methods,
            @PathVariable String group,
            @Parameter(description = "Accepted values: [category, method, user, month, year]") @PathVariable String keyPattern
            ) {
        ExpenseFilter filter = new ExpenseFilter();
        filter.setGroupName(group);
        filter.setEmails(emails);
        filter.setCategoryNames(categoryNames);
        filter.setMethodsOfPayment(methods);
        if (beginDate != null) filter.setBeginDate(LocalDate.parse(beginDate));
        if (endDate != null) filter.setEndDate(LocalDate.parse(endDate));
        return new ResponseEntity<>(service.getMapResult(filter, keyPattern), HttpStatus.OK);
    }
}