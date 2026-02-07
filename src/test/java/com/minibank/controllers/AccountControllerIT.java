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

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        CreateAccountRequest request = new CreateAccountRequest();

        request.setClientName("Ivan Ivanov");
        request.setInitialBalance(new BigDecimal("1000.00"));

        String responseJson = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponse createdAccount = objectMapper.readValue(
                responseJson,
                AccountResponse.class
        );

        System.out.println(createdAccount);
        Long createdAccountId = createdAccount.getId();

        mockMvc.perform(get("/api/accounts/{id}", createdAccountId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdAccountId))
                .andExpect(jsonPath("$.clientName").value("Ivan Ivanov"))
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    void shouldReturnNotFoundWhenAccountDoesNotExist() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        Long nonExistentId = 999L;
        mockMvc.perform(get("/api/accounts/{id}", nonExistentId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Account not found"))
                .andExpect(jsonPath("$.message").value("Account not found with id: " + nonExistentId));
    }
}
