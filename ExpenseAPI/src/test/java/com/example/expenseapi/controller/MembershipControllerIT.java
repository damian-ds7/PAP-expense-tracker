package com.example.expenseapi.controller;

import com.example.expenseapi.repository.MembershipRepository;
import com.example.expenseapi.service.MembershipService;
import com.example.expenseapi.utils.TokenGenerator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class MembershipControllerIT {
    private final MockMvc mockMvc;
    private final String admin = "herkules1@gmail.com";
    private final String member = "herkules2@gmail.com";
    private final TokenGenerator gen;
    private final MembershipRepository membershipRepository;

    @Autowired
    public MembershipControllerIT(MockMvc mockMvc, TokenGenerator gen, MembershipRepository membershipRepository) {
        this.mockMvc = mockMvc;
        this.gen = gen;
        this.membershipRepository = membershipRepository;
    }

    @Test
    void testInvite_UserNotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/membership/invite"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Transactional
    @Test
    void testInvite_InvitationFromAdminNewUser() throws Exception {
        String jsonBody = """
                {
                    "user": {
                        "id": 4,
                        "name": "Herkules4",
                        "surname": "Herkules4",
                        "email": "herkules4@gmail.com"
                    },
                    "group": {
                        "id": 1,
                        "name": "family"
                    }
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/membership/invite")
                        .header("Authorization", "Bearer " + gen.getToken(admin))
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sender.name").value("Herkules1"));
    }


    @Test
    void testInvite_InvitationFromAdminUserAlreadyInGroup() throws Exception {
        String jsonBody = """
                {
                    "user": {
                        "id": 1,
                        "name": "Herkules2",
                        "surname": "Herkules2",
                        "email": "herkules2@gmail.com"
                    },
                    "group": {
                        "id": 1,
                        "name": "family"
                    }
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/membership/invite")
                        .header("Authorization", "Bearer " + gen.getToken(admin))
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Transactional
    @Test
    void testInvite_InvitationFromAdminUserAlreadyInvited() throws Exception {
        String jsonBody = """
                {
                    "user": {
                        "id": 4,
                        "name": "Herkules4",
                        "surname": "Herkules4",
                        "email": "herkules4@gmail.com"
                    },
                    "group": {
                        "id": 1,
                        "name": "family"
                    }
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/membership/invite")
                        .header("Authorization", "Bearer " + gen.getToken(admin))
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/membership/invite")
                        .header("Authorization", "Bearer " + gen.getToken(admin))
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }



    @Test
    void testInvite_InvitationFromMember() throws Exception {
        String jsonBody = """
                {
                    "user": {
                        "id": 4,
                        "name": "Herkules4",
                        "surname": "Herkules4",
                        "email": "herkules4@gmail.com"
                    },
                    "group": {
                        "id": 1,
                        "name": "family"
                    }
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/membership/invite")
                        .header("Authorization", "Bearer " + gen.getToken(member))
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testInvite_UserNotFound() throws Exception {
        String jsonBody = """
                {
                    "user": {
                        "id": 5,
                        "name": "Herkules5",
                        "surname": "Herkules5",
                        "email": "herkules5@gmail.com"
                    },
                    "group": {
                        "id": 1,
                        "name": "family"
                    }
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/membership/invite")
                        .header("Authorization", "Bearer " + gen.getToken(admin))
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testInvite_GroupNotFound() throws Exception {
        String jsonBody = """
                {
                    "user": {
                        "id": 4,
                        "name": "Herkules4",
                        "surname": "Herkules4",
                        "email": "herkules4@gmail.com"
                },
                    "group": {
                        "id": 1,
                        "name": "unknown"
                    }
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/membership/invite")
                        .header("Authorization", "Bearer " + gen.getToken(admin))
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    void testDelete_UserNotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/membership/delete/2/family"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testDelete_UserNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/membership/delete/1/family")
                        .header("Authorization", "Bearer " + gen.getToken(member)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    void testDelete_UserAdminDeleteMember() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/membership/delete/2/family")
                        .header("Authorization", "Bearer " + gen.getToken(admin)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertFalse(membershipRepository.findAdmins("family").stream().anyMatch(user -> user.getId().equals(2L)));
    }

    @Test
    @Transactional
    void testDelete_UserAdminDeleteAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/membership/delete/1/family")
                        .header("Authorization", "Bearer " + gen.getToken(admin)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertFalse(membershipRepository.findUsers("family").stream().anyMatch(user -> user.getId().equals(1L)));
    }

    @Test
    void testDelete_UserNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/membership/delete/99/family")
                        .header("Authorization", "Bearer " + gen.getToken(admin)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void testDelete_GroupNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/membership/delete/2/unknown")
                        .header("Authorization", "Bearer " + gen.getToken(admin)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void testUpdate_UserNotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/membership/update"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
