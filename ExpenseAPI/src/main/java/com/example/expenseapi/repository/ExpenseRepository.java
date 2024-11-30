package com.example.expenseapi.repository;


import com.example.expenseapi.pojo.Expense;
import org.springframework.data.repository.CrudRepository;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {


}
