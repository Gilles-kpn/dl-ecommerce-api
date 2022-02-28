package fr.gilles.dleapi.controllers;

import fr.gilles.dleapi.security.AuthSecurity;
import fr.gilles.dleapi.services.product.CategoryService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
class CategoryControllerTest {
    @Autowired
    private  MockMvc mockMvc;

    @Test
    void  testGet() throws Exception {
        mockMvc.perform(get("/category")).andExpect(status().isOk());
    }



}
