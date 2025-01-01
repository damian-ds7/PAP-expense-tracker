package com.example.expenseapi.utils;

import com.example.expenseapi.dto.ExpenseCreateDTO;
import com.example.expenseapi.dto.ExpenseDTO;
import com.example.expenseapi.pojo.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    @Mapping(source = "membership.group.name", target = "groupName")
    @Mapping(source = "membership.user.email", target = "userEmail")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "method.name", target = "methodOfPayment")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "date", target = "expenseDate")
    @Mapping(source = "membership.user.name", target = "name")
    @Mapping(source = "membership.user.surname", target = "surname")
    ExpenseDTO expenseToExpenseDTO(Expense expense);

    @Mapping(source = "categoryName", target = "category.name")
    @Mapping(source = "methodOfPayment", target = "method.name")
    @Mapping(source = "currencyCode", target = "currency.symbol")
    @Mapping(source = "membershipId", target = "membership.id")
    Expense expenseCreateDTOToExpense(ExpenseCreateDTO dto);
}
