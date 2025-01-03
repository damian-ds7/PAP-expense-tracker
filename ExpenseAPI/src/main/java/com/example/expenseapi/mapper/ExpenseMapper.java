package com.example.expenseapi.mapper;

import com.example.expenseapi.dto.ExpenseCreateDTO;
import com.example.expenseapi.dto.ExpenseDTO;
import com.example.expenseapi.pojo.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    @Mapping(source = "membership.group.name", target = "groupName")
    @Mapping(source = "membership.user.email", target = "user.email")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "method.name", target = "methodOfPayment")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "date", target = "expenseDate")
    @Mapping(source = "membership.user.name", target = "user.name")
    @Mapping(source = "membership.user.surname", target = "user.surname")
    @Mapping(source = "membership.user.id", target = "user.id")
    ExpenseDTO expenseToExpenseDTO(Expense expense);

    @Mapping(source = "categoryName", target = "category.name")
    @Mapping(source = "methodOfPayment", target = "method.name")
    @Mapping(source = "currencyCode", target = "currency.symbol")
    @Mapping(source = "groupName", target = "membership.group.name")
    @Mapping(source = "user.id", target = "membership.user.id")
    Expense expenseCreateDTOToExpense(ExpenseCreateDTO dto);
}
