package com.minibank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibank.dto.AccountResponse;
import com.minibank.dto.CreateAccountRequest;
import com.minibank.dto.TransferRequest;
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
public class TransferControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void transferEndpointShouldWork() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1L);
        request.setToAccountId(2L);
        request.setAmount(new BigDecimal("100.00"));
        request.setComment("test transfer");

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Account not found"));
    }


    @Test
    void shouldTransferMoneySuccessfully() throws Exception {
        // arrange
        AccountResponse sender = createAccount("Sender", "1000.00");
        AccountResponse receiver = createAccount("Receiver", "10.00");

        // act
        BigDecimal transferAmount = new BigDecimal("500.00");
        String comment = "test payment";

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromAccountId(sender.getId());
        transferRequest.setToAccountId(receiver.getId());
        transferRequest.setAmount(transferAmount);
        transferRequest.setComment(comment);

        // act & assert
        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fromAccountId").value(sender.getId()))
                .andExpect(jsonPath("$.toAccountId").value(receiver.getId()))
                .andExpect(jsonPath("$.amount").value(500.00))
                .andExpect(jsonPath("$.comment").value(comment));

        // assert
        verifyAccountBalance(sender.getId(), 500.00);
        verifyAccountBalance(receiver.getId(), 510.00);
    }


    @Test
    void shouldReturnBadRequestWhenTransferSameAccount() throws Exception {
        AccountResponse account = createAccount("Sender", "1000.00");
        BigDecimal transferAmount = new BigDecimal("500.00");
        String comment = "test bad 400 payment";

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromAccountId(account.getId());
        transferRequest.setToAccountId(account.getId());
        transferRequest.setAmount(transferAmount);
        transferRequest.setComment(comment);

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Transfer failed"))
                .andExpect(jsonPath("$.message").value("Cannot transfer to the same account"));
    }

    @Test
    void shouldReturnBadRequestWhenInsufficientFunds() throws Exception {
        AccountResponse sender = createAccount("sender", "10.00");
        AccountResponse receiver = createAccount("sender", "1000.00");

        BigDecimal transferAmount = new BigDecimal("100.00");
        String comment = "hack money";

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromAccountId(sender.getId());
        transferRequest.setToAccountId(receiver.getId());
        transferRequest.setAmount(transferAmount);
        transferRequest.setComment(comment);

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Transfer failed"))
                .andExpect(jsonPath("$.message").value("insufficient funds"));
    }

    private AccountResponse createAccount(String clientName, String initialBalance) throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setClientName(clientName);
        request.setInitialBalance(new BigDecimal(initialBalance));

        String responseJson = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(responseJson, AccountResponse.class);
    }

    private void verifyAccountBalance(Long accountId, double expectedBalance) throws Exception {
        mockMvc.perform(get("/api/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId))
                .andExpect(jsonPath("$.balance").value(expectedBalance));
    }
}
