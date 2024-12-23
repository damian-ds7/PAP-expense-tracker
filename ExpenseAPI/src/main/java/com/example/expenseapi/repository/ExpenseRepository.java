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
            "(SELECT m.user FROM Membership m WHERE m.group.name = :groupName)" +
            "AND e.category.name = :category")
    List<Expense> findByCategoryName(String category, String groupName);

    @Query("SELECT e FROM Expense e " +
            "where e.user IN " +
            "(SELECT m.user FROM Membership m WHERE m.group.name = :groupName)" +
            "AND e.date = :date")
    List<Expense> findByDate(LocalDate date, String groupName);

    @Query("SELECT e FROM Expense e " +
            "where e.user IN " +
            "(SELECT m.user FROM Membership m WHERE m.group.name = :groupName)" +
            "AND e.date between :begin and :end")
    List<Expense> findByDateBetween(LocalDate begin, LocalDate end, String groupName);

    @Query("SELECT e FROM Expense e " +
            "where e.user IN " +
            "(SELECT m.user FROM Membership m WHERE m.group.name = :groupName)" +
            "AND e.price between :leftEnd and :rightEnd")
    List<Expense> findByPriceBetween(double leftEnd, double rightEnd, String groupName);

    @Query("SELECT e FROM Expense e " +
            "where e.user IN " +
            "(SELECT m.user FROM Membership m WHERE m.group.name = :groupName)" +
            "AND e.price <= :price")
    List<Expense> findByPriceLessThan(double price, String groupName);

    @Query("SELECT e FROM Expense e " +
            "where e.user IN " +
            "(SELECT m.user FROM Membership m WHERE m.group.name = :groupName)" +
            "AND e.price >= :price")
    List<Expense> findByPriceGreaterThan(double price, String groupName);
//    List<Expense> findAllByOrderByDateDesc(String groupName);

    @Query("SELECT e FROM Expense e WHERE e.user IN (SELECT m.user FROM Membership m WHERE m.name = :name)")
    List<Expense> findByUserGroupName(String name);

    @Query("SELECT e FROM Expense e " +
            "WHERE e.user.email = :email " +
            "AND EXTRACT(YEAR FROM e.date) = :year")
    List<Expense> findByUserEmailAndYear(String email, String year);

    @Query("SELECT TO_CHAR(e.date, 'Month'), SUM(e.price) " +
            "FROM Expense e " +
            "WHERE e.user IN " +
            "(SELECT m.user FROM Membership m WHERE m.group.name = :groupName) " +
            "AND EXTRACT(YEAR FROM e.date) = :year " +
            "GROUP BY TO_CHAR(e.date, 'Month'), EXTRACT(MONTH FROM e.date)")
    List<Object[]> findTotalExpensesForMonthsGroup(String year, String groupName);
}