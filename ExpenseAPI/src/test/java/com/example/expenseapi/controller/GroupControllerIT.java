package com.example.expenseapi.controller;

import com.example.expenseapi.utils.TokenGenerator;
import org.hamcrest.Matchers;
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
public class GroupControllerIT {
    private final MockMvc mockMvc;
    private final TokenGenerator gen;
    private final String user1 = "herkules1@gmail.com"; // family and workers as active groups and family2 as archived
    private final String user2 = "herkules4@gmail.com"; // empty group


    @Autowired
    public GroupControllerIT(MockMvc mockMvc, TokenGenerator gen) {
        this.mockMvc = mockMvc;
        this.gen = gen;
    }

    @Test
    void testGroupAll_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/all/base"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testGroupAll_NoGroups() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/all/base")
                        .header("Authorization", "Bearer " + gen.getToken(user2)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void testGroupAll_HasGroups() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/all/base")
                        .header("Authorization", "Bearer " + gen.getToken(user1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", Matchers.containsInAnyOrder("family", "workers", "members-only")));

    }

    @Test
    void testGroupAllActive_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/all/active"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testGroupAllActive_NoGroups() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/all/active")
                        .header("Authorization", "Bearer " + gen.getToken(user2)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void testGroupAllActive_HasGroups() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/all/active")
                        .header("Authorization", "Bearer " + gen.getToken(user1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", Matchers.containsInAnyOrder("family", "workers", "members-only")));
    }

    @Test
    void testGroupMembers_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/members/any"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testGroupMembers_UnknownGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/members/unknown")
                        .header("Authorization", "Bearer " + gen.getToken(user1)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testGroupMembers_MembersPresentFirstGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/members/family")
                        .header("Authorization", "Bearer " + gen.getToken(user1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].email", Matchers.containsInAnyOrder("herkules1@gmail.com", "herkules2@gmail.com")));
    }

    @Test
    void testGroupMembers_MembersPresentSecondGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/members/workers")
                        .header("Authorization", "Bearer " + gen.getToken(user1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].email", Matchers.containsInAnyOrder("herkules1@gmail.com", "herkules2@gmail.com","herkules3@gmail.com")));
    }

    @Test
    void testGroupMembers_UserNotInGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/members/family")
                        .header("Authorization", "Bearer " + gen.getToken(user2)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testGroupAdmins_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/admins/any"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testGroupAdmins_UnknownGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/admins/unknown")
                        .header("Authorization", "Bearer " + gen.getToken(user1)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testGroupAdmins_AdminPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/admins/family")
                        .header("Authorization", "Bearer " + gen.getToken(user1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].email", Matchers.containsInAnyOrder("herkules1@gmail.com")));
    }

    @Test
    void testGroupAdmins_NoAdminsPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/admins/members-only")
                        .header("Authorization", "Bearer " + gen.getToken(user1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotExist());
    }

    @Test
    void testGroupAdmins_UserNotInGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/group/admins/family")
                        .header("Authorization", "Bearer " + gen.getToken(user2)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}


