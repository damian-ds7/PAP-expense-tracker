package com.example.expenseapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerIT {
    private final MockMvc mockMvc;

    @Autowired
    public CategoryControllerIT(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void testAllCategories() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("food"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("transport"));
    }
}
