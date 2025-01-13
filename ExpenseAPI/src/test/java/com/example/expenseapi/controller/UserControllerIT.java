package com.example.expenseapi.controller;

import static org.assertj.core.api.Assertions.assertThat;
import com.example.expenseapi.repository.UserRepository;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class UserControllerIT {
    private final MockMvc mockMvc;
    private final TokenGenerator gen;
    private final UserRepository userRepository;
    private final String activeUser = "herkules1@gmail.com";

    @Autowired
    public UserControllerIT(MockMvc mockMvc, TokenGenerator gen, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.mockMvc = mockMvc;
        this.gen = gen;
    }

//    @Test
//    void testSearch_NotLoggedIn() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/any"))
//                .andExpect(MockMvcResultMatchers.status().isForbidden());
//    }
//
//    @Test
//    void testSearch_WrongGroup() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/unknown")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isForbidden());
//    }
//
//    @Test
//    void testSearch_ExactName() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?name=Herkules3")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Herkules3"));
//    }
//
//    @Test
//    void testSearch_ExactNameLowered() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?name=herkules3")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Herkules3"));
//    }
//
//    @Test
//    void testSearch_ExactNameUpper() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?name=HERKULES3")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Herkules3"));
//    }
//
//
//    @Test
//    void testSearch_NotExactName() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?name=Herk")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", Matchers.containsInAnyOrder("Herkules3", "Herkules4")));
//    }
//
//    @Test
//    void testSearch_NotExactNameLowered() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?name=herk")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", Matchers.containsInAnyOrder("Herkules3", "Herkules4")));
//    }
//
//    @Test
//    void testSearch_NotExactNameUpper() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?name=HERK")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", Matchers.containsInAnyOrder("Herkules3", "Herkules4")));
//    }
//
//    @Test
//    void testSearch_InvalidName() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?name=Herkules5")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
//    }
//
//    @Test
//    void testSearch_ExactSurname() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?surname=Herkules3")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("Herkules3"));
//    }
//
//    @Test
//    void testSearch_ExactSurnameLowered() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?surname=herkules3")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("Herkules3"));
//    }
//
//    @Test
//    void testSearch_ExactSurnameUpper() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?surname=HERKULES3")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("Herkules3"));
//    }
//
//    @Test
//    void testSearch_NotExactSurname() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?surname=Herk")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[*].surname", Matchers.containsInAnyOrder("Herkules3", "Herkules4")));
//    }
//
//    @Test
//    void testSearch_NotExactSurnameLowered() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?surname=herk")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[*].surname", Matchers.containsInAnyOrder("Herkules3", "Herkules4")));
//    }
//
//    @Test
//    void testSearch_NotExactSurnameUpper() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?surname=HERK")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[*].surname", Matchers.containsInAnyOrder("Herkules3", "Herkules4")));
//    }
//
//    @Test
//    void testSearch_InvalidSurname() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/user/search/family?surname=Herkules5")
//                .header("Authorization", "Bearer " + gen.getToken(activeUser)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
//    }

    @Test
    void testChangePass_NotLoggedIn() throws Exception {
        String jsonBody = """
                {
                    "newPassword": "newPass"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changePass")
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testChangePass_NoNewPass() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changePass")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Transactional
    @Test
    void testChangePass_Success() throws Exception {
        String startPassword = userRepository.findByEmail(activeUser).get().getPassword();
        String jsonBody = """
                {
                    "newPassword": "newPassword"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changePass")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(activeUser));
        String endPassword = userRepository.findByEmail(activeUser).get().getPassword();
        assertThat(startPassword).isNotEqualTo(endPassword);
    }

    @Test
    void testUpdate_NotLoggedIn() throws Exception {
        String jsonBody = """
                {
                    "name": "newName",
                    "surname": "newSurname"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.put("/user/update")
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testUpdate_EmptyBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/update")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Transactional
    @Test
    void testUpdate_ChangeName() throws Exception {
        String jsonBody = """
                {
                    "name": "newName"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.put("/user/update")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("newName"));
    }

    @Transactional
    @Test
    void testUpdate_ChangeSurname() throws Exception {
        String jsonBody = """
                {
                    "surname": "newSurname"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.put("/user/update")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("newSurname"));
    }

    @Transactional
    @Test
    void testUpdate_ChangeEmailAlreadyPresent() throws Exception {
        String jsonBody = """
                {
                    "email": "herkules2@gmail.com"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.put("/user/update")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Transactional
    @Test
    void testUpdate_ChangeEmailFree() throws Exception {
        String jsonBody = """
                {
                    "email": "herkules5@gmail.com"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.put("/user/update")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("herkules5@gmail.com"));

    }

    @Transactional
    @Test
    void testUpdate_ChangeAll() throws Exception {
        String jsonBody = """
                {
                    "name": "newName",
                    "surname": "newSurname",
                    "email": "herkules5@gmail.com"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.put("/user/update")
                .header("Authorization", "Bearer " + gen.getToken(activeUser))
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("newName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("newSurname"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("herkules5@gmail.com"));
    }

    @Test
    void testIsAdmin_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/isAdmin"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testIsAdmin_Admin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/isAdmin/family")
                        .header("Authorization", "Bearer " + gen.getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void testIsAdmin_NotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/isAdmin/workers")
                        .header("Authorization", "Bearer " + gen.getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    @Test
    void testIsAdmin_UnknownGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/isAdmin/unknown")
                        .header("Authorization", "Bearer " + gen.getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testCurrent_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/current"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testCurrent_LoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/current")
                        .header("Authorization", "Bearer " + gen.getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(activeUser))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Herkules1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("Herkules1"));
    }

    @Test
    void testChangeRole_NotLoggedIn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changeRole/family/USER/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testChangeRole_NotAdminToAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changeRole/workers/3/admin")
                        .header("Authorization", "Bearer " + gen.getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testChangeRole_NotAdminToMember() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changeRole/workers/2/member")
                        .header("Authorization", "Bearer " + gen.getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Transactional
    @Test
    void testChangeRole_AdminToAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changeRole/family/2/admin")
                        .header("Authorization", "Bearer " + gen.getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Transactional
    @Test
    void testChangeRole_AdminToMember() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changeRole/family/2/member")
                        .header("Authorization", "Bearer " + gen.getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testChangeRole_UnknownGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changeRole/unknown/2/member")
                        .header("Authorization", "Bearer " + gen.getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testChangeRole_UnknownUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changeRole/family/0/member")
                        .header("Authorization", "Bearer " + gen.getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testChangeRole_UnknownRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changeRole/family/2/unknown")
                        .header("Authorization", "Bearer " + gen.getToken(activeUser)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

