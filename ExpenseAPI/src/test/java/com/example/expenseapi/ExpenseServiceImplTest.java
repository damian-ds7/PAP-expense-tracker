package com.example.expenseapi;

import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.pojo.User;
import com.example.expenseapi.repository.ExpenseRepository;
import com.example.expenseapi.service.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

public class ExpenseServiceImplTest {
    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetExpensesByEmail() {
        User user = new User("Herkules1", "Herkules1", "herkules1@gmail.com");
        List<Expense> expList = Arrays.asList(new Expense(200, user), new Expense(300, user));
        when(expenseRepository.findAll()).thenReturn(expList);
        List<Expense> result = expenseService.getExpensesByEmail(user.getEmail());
        assertEquals(2, result.size());
        assertEquals(200, result.getFirst().getPrice());
        assertEquals(300, result.get(1).getPrice());
    }

    @Test
    public void testGEtExpanseByEmailEmpty() {
        User user = new User("Herkules1", "Herkules1", "herkules1@gmail.com");
        User user1 = new User("Herkules2", "Herkules2", "herkules2@gmail.com");
        List<Expense> expList = Arrays.asList(new Expense(200, user), new Expense(300, user));
        when(expenseRepository.findAll()).thenReturn(expList);
        assertEquals(0, expenseService.getExpensesByEmail(user1.getEmail()).size());
        assertEquals(2, expenseService.getExpensesByEmail(user.getEmail()).size());
    }
}
