package com.example.expenseapi.controller;

import com.example.expenseapi.utils.TokenGenerator;
import jakarta.transaction.Transactional;
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
public class PreferencesControllerIT {
    private final MockMvc mockMvc;
    private final TokenGenerator gen;
    private final String activeUser = "herkules1@gmail.com";

    @Autowired
    public PreferencesControllerIT(MockMvc mockMvc, TokenGenerator gen) {
        this.mockMvc = mockMvc;
        this.gen = gen;
    }

    @Test
    void testPreferencesCurrent_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/preferences/current"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testPreferencesCurrent_LoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/preferences/current")
                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.method.name").value("cash"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency.symbol").value("PLN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.language").value("pl"));
    }

    @Test
    void testPreferencesUpdate_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/preferences/update")
                .content("""
                        {
                            "methodOfPayment": "cash",
                            "currencySymbol": "USD",
                            "language": "en"
                        }
                        """)
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testPreferencesUpdate_EmptyBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/preferences/update")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Transactional
    @Test
    void testPreferencesUpdate_MethodPresentInDatabase() throws Exception {
        String jsonBody = """
                {
                    "methodOfPayment": "debit card"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.put("/preferences/update")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .content(jsonBody)
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.method.name").value("debit card"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency.symbol").value("PLN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.language").value("pl"));
    }

    @Transactional
    @Test
    void testPreferencesUpdate_MethodNotPresentInDatabase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/preferences/update")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .content("""
                        {
                            "methodOfPayment": "crypto"
                        }
                        """)
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Transactional
    @Test
    void testPreferencesUpdate_CurrencyPresentInDatabase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/preferences/update")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .content("""
                        {
                            "currencySymbol": "USD"
                        }
                        """)
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.method.name").value("cash"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency.symbol").value("USD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.language").value("pl"));
    }

    @Transactional
    @Test
    void testPreferenceUpdate_InvalidCurrencyFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/preferences/update")
                        .header("Authorization", "Bearer " + gen.getToken(activeUser))
                        .content("""
                        {
                            "currencySymbol": "abcdef"
                        }
                        """)
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Transactional
    @Test
    void testPreferencesUpdate_CurrencyNotPresentInDatabase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/preferences/update")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .content("""
                        {
                            "currencySymbol": "THB"
                        }
                        """)
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }



    @Transactional
    @Test
    void testPreferencesUpdate_Language() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/preferences/update")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .content("""
                        {
                            "language": "en"
                        }
                        """)
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.method.name").value("cash"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency.symbol").value("PLN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.language").value("en"));
    }
}
