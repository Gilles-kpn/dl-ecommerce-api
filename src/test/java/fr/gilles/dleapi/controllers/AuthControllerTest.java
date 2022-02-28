package fr.gilles.dleapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.gilles.dleapi.payloader.user.LoginUser;
import fr.gilles.dleapi.payloader.user.UserCreate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    private String auth;



    private static final String URL = "/auth";


    @Test
    @BeforeAll
    void login() throws Exception {
        LoginUser loginUser = new LoginUser();
        loginUser.setPassword("Gkpanou2@gmail.ce");
        loginUser.setEmail("admin@admin.com");
        mockMvc.perform(
                post(URL +"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                             objectMapper.writeValueAsString(loginUser)
                        )
                        .with(csrf())
        ).andExpect(status().isOk())
                .andExpect(result -> {
                    auth = result.getResponse().getContentAsString();
                })
                .andDo(print());
    }






    @Test
    void current() throws Exception {
        mockMvc.perform(
                get(URL +"/current")
                        .header("Authorization", auth)
        ).andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    void register() throws Exception {
        UserCreate userCreate = new UserCreate();
        userCreate.setEmail("admin@admin.com");
        userCreate.setPassword("Gkpanou2@gmail.ce");
        userCreate.setName("KPANOU Gilles");

        mockMvc.perform(
                post(URL +"/register")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(userCreate))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isConflict());

    }

    @Test
    void currentRole() throws Exception {
        mockMvc.perform(
                get(URL +"/current/role")
                        .header("Authorization", auth)
        ).andExpect(status().isOk())
                .andDo(print());
    }

}
