package com.example.expenseapi.repository;

import com.example.expenseapi.pojo.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserEmail(String email);
    Optional<Expense> findTopByOrderByIdDesc();

    @Query("SELECT e FROM Expense e " +
            "where e.user IN " +
            "(SELECT m.user FROM Membership m WHERE m.name = :groupName)" +
            "AND e.category.name = :category")
    List<Expense> findByCategoryName(String category, String groupName);

//    List<Expense> findByDate(LocalDate date, String groupName);
//    List<Expense> findByDateBetween(LocalDate begin, LocalDate end, String groupName);
//    List<Expense> findByPriceBetween(double leftEnd, double rightEnd, String groupName);
//    List<Expense> findByPriceLessThan(double price, String groupName);
//    List<Expense> findByPriceGreaterThan(double price, String groupName);
//    List<Expense> findAllByOrderByDateDesc(String groupName);

    @Query("SELECT e FROM Expense e WHERE e.user IN (SELECT m.user FROM Membership m WHERE m.name = :name)")
    List<Expense> findByUserGroupName(String name);
}