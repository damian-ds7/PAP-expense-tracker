package com.example.expenseapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIT {
    private final MockMvc mockMvc;

    @Autowired
    public AuthControllerIT(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void testNoData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void testLoginSuccessful() throws Exception {
        String jsonRequestBody = """
                {
                    "email": "herkules1@gmail.com",
                    "password": "123"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .content(jsonRequestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }


    @Test
    void testLoginWrongEmail() throws Exception {
        String jsonRequestBody = """
                {
                    "email": "herkules2@gmail.com",
                    "password": "123"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .content(jsonRequestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testLoginWrongPassword() throws Exception {
        String jsonRequestBody = """
                {
                    "email": "herkules1@gmail.com",
                    "password": "1234"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .content(jsonRequestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    @Test
    void testRegisterSuccessful() throws Exception {
        String jsonRequestBody = """
                {
                    "name": "Jurek",
                    "surname": "Ogorek",
                    "email": "jurekogorek2@gmail.com",
                    "password": "rura"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .content(jsonRequestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
    @Test
    void testRegisterEmailConflict() throws Exception {
        String jsonRequestBody = """
                {
                    "name": "Jurek",
                    "surname": "Ogorek",
                    "email": "herkules1@gmail.com",
                    "password": "rura"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .content(jsonRequestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
}
