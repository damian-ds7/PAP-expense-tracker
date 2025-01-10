package com.example.expenseapi.controller;

import static org.assertj.core.api.Assertions.assertThat;
import com.example.expenseapi.repository.RefreshTokenRepository;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
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
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public AuthControllerIT(MockMvc mockMvc, RefreshTokenRepository refreshTokenRepository) {
        this.mockMvc = mockMvc;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Test
    void testNoData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Transactional
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
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").exists());
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

    @Test
    void testRefreshUserWithActiveToken() throws Exception {
        String jsonRequestBody = """
                {
                "refreshToken": "token1"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                .content(jsonRequestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

    @Test
    void testRefreshUserWithExpiredToken() throws Exception {
        String jsonRequestBody = """
                {
                "refreshToken": "token2"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                .content(jsonRequestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        assertThat(refreshTokenRepository.count()).isEqualTo(1);
    }

    @Test
    void testRefreshUserWithNonExistingToken() throws Exception {
        String jsonRequestBody = """
                {
                "refreshToken": "token3"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                .content(jsonRequestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testRefreshUserWithEmptyToken() throws Exception {
        String jsonRequestBody = """
                {
                "refreshToken": ""
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                .content(jsonRequestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testLoginAndRefresh() throws Exception {
        String jsonRequestBody = """
                {
                    "email": "herkules1@gmail.com",
                    "password": "123"
                }
                """;
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .content(jsonRequestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        String refreshToken = JsonPath.read(result, "$.refreshToken");
        String jsonRequestBodyRefresh = """
                {
                "refreshToken": "%s"
                }
                """.formatted(refreshToken);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                        .content(jsonRequestBodyRefresh)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

}
