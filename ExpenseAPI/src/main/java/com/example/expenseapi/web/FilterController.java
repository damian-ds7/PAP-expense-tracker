package com.example.expenseapi.web;

import com.example.expenseapi.dto.ExpenseDTO;
import com.example.expenseapi.filter.ExpenseFilter;
import com.example.expenseapi.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Retrieves expenses which satisfies conditions")
    @ApiResponse(responseCode = "200", description = "List of expense objects satisfying conditions", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExpenseDTO.class))))
    public ResponseEntity<List<ExpenseDTO>> search(
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String beginDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) List<String> methods,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) List<String> emails
    ) {
        ExpenseFilter filter = new ExpenseFilter();
        filter.setCategoryNames(categories);
        filter.setGroupName(groupName);
        filter.setPriceMin(minPrice);
        filter.setPriceMax(maxPrice);
        filter.setEmail(email);
        filter.setMethodsOfPayment(methods);
        if (groupName != null) filter.setEmails(emails);
        if (beginDate != null) filter.setBeginDate(LocalDate.parse(beginDate));
        if (endDate != null) filter.setEndDate(LocalDate.parse(endDate));

        return new ResponseEntity<>(service.searchExpensesDTO(filter), HttpStatus.OK);
    }
}
