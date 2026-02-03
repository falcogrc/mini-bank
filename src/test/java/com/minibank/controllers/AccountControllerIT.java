package com.minibank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibank.dto.AccountResponse;
import com.minibank.dto.CreateAccountRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAccountAndReturnCreatedStatus() throws Exception {
        // given
        CreateAccountRequest request = new CreateAccountRequest();
        request.setClientName("Ivan Ivanov");
        request.setInitialBalance(new BigDecimal("1000.00"));

        // when & then
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientName").value("Ivan Ivanov"))
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingAccountWithInvalidData() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setClientName(null);
        request.setInitialBalance(new BigDecimal("-123.45"));

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnAccountById() throws Exception {
        // TODO: напиши тест:
        // 1. Создать счет через POST
        CreateAccountRequest request = new CreateAccountRequest();
        request.setClientName("Ivan Ivanov");
        request.setInitialBalance(new BigDecimal("1000.00"));

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // 2. Получить его ID из ответа
        // 3. Вызвать GET /api/accounts/{id}
        mockMvc.perform(get("/api/accounts/{id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        // 4. Проверить, что данные совпадают
    }

    @Test
    void shouldReturnNotFoundWhenAccountDoesNotExist() {
        // TODO: доделать
    }
}
