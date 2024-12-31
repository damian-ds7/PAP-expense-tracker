package com.example.expenseapi.controller;

import com.example.expenseapi.utils.JwtUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
/* Tests use database which schema is defined in ExpenseApiApplication (only with this schema they make sense) */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc

public class ExpanseControllerIT {
    private final MockMvc mockMvc;
    private final String activeUser = "herkules1@gmail.com"; // This user has certain expenses and belongs to two groups - family and workers
    private final String inactiveUser = "herkules4@gmail.com"; // This user has no expenses and doesn't belong to any groups


    @Autowired
    public ExpanseControllerIT(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Autowired
    private JwtUtil jwtUtil;

    private String getToken(String email) {
        return jwtUtil.generateToken(email);
    }

    @Test
    void testMyExpenses_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/my/expenses"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testMyExpenses_ExpensesPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/my/expenses")
                        .header("Authorization", "Bearer " + getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    void testMyExpenses_NoExpensesPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/my/expenses")
                        .header("Authorization", "Bearer " + getToken(inactiveUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotExist());
    }

    @Test
    void testMyGroupExpenses_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/my/group/any"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testMyGroupExpenses_ExpensesPresentFirstGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/my/group/family")
                        .header("Authorization", "Bearer " + getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    void testMyGroupExpenses_ExpensesPresentSecondGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/my/group/workers")
                        .header("Authorization", "Bearer " + getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    void testMyGroupExpenses_UnknownGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/my/group/any")
                        .header("Authorization", "Bearer " + getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotExist());
    }

    @Test
    void testMyGroupExpenses_NoExpensesPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/my/group/any")
                        .header("Authorization", "Bearer " + getToken(inactiveUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotExist());
    }

    @Test
    void testState_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/state/allGroups"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testState_LoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/state/allGroups")
                        .header("Authorization", "Bearer " + getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userExpenses").value(150))
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupExpenses").value(950));
    }

    @Test
    void testState_NoExpensesPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/state/allGroups")
                        .header("Authorization", "Bearer " + getToken(inactiveUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userExpenses").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupExpenses").value(0));
    }

    @Test
    void testStateGroup_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/state/family"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testStateGroup_ExpensesPresentFirstGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/state/family")
                        .header("Authorization", "Bearer " + getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userExpenses").value(150))
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupExpenses").value(350));
    }

    @Test
    void testStateGroup_ExpensesPresentSecondGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/state/workers")
                        .header("Authorization", "Bearer " + getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userExpenses").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupExpenses").value(600));
    }

    @Test
    void testStateGroup_UnknownGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/state/any")
                        .header("Authorization", "Bearer " + getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userExpenses").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupExpenses").value(0));
    }

    @Test
    void testStateGroup_NoExpensesPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/state/any")
                        .header("Authorization", "Bearer " + getToken(inactiveUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userExpenses").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupExpenses").value(0));
    }

    @Test
    void testDateMap_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/dateMap/group/family"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testDateMap_ExpensesPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/dateMap/group/family")
                        .header("Authorization", "Bearer " + getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(3)));
    }

    @Test
    void testDateMap_UnknownGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/dateMap/group/any")
                        .header("Authorization", "Bearer " + getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").doesNotExist());
    }

    @Test
    void tesDateMap_NoExpensesPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/dateMap/group/family")
                        .header("Authorization", "Bearer " + getToken(inactiveUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").doesNotExist());
    }

    @Test
    void testCategoryMap_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/categoryMap/group/family"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testCategoryMap_ExpensesPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/categoryMap/group/family")
                        .header("Authorization", "Bearer " + getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(2)));
    }

    @Test
    void testCategoryMap_UnknownGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/categoryMap/group/any")
                        .header("Authorization", "Bearer " + getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").doesNotExist());
    }

    @Test
    void testCategoryMap_NoExpensesPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/categoryMap/group/family")
                        .header("Authorization", "Bearer " + getToken(inactiveUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").doesNotExist());
    }

    @Test
    void testRecent_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/recent"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testRecent_ExpensesPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/recent")
                        .header("Authorization", "Bearer " + getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2025-11-30"));
    }

    @Test
    void testRecent_NoExpensesPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/expense/recent")
                        .header("Authorization", "Bearer " + getToken(inactiveUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }
}
