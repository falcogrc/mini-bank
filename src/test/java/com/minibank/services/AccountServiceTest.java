package com.minibank.services;

import com.minibank.dto.AccountResponse;
import com.minibank.dto.CreateAccountRequest;
import com.minibank.models.BankAccount;
import com.minibank.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void shouldCreateAccountSuccessfully() {
        // given
        CreateAccountRequest request = new CreateAccountRequest();
        request.setClientName("Ivan Ivanov");
        request.setInitialBalance(new BigDecimal("1000.00"));

        BankAccount savedAccount = new BankAccount(
                1L,
                "Ivan Ivanov",
                new BigDecimal("1000.00"),
                LocalDateTime.now()
        );

        when(accountRepository.save(any(BankAccount.class))).thenReturn(savedAccount);

        // when
        AccountResponse response = accountService.createAccount(request);

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getClientName()).isEqualTo("Ivan Ivanov");
        assertThat(response.getBalance()).isEqualByComparingTo("1000.00");
    }

    @Test
    void shouldGetAccountById() {
        Long accountId = 1L;
        BankAccount bankAccount = new BankAccount(
                accountId,
                "Test",
                new BigDecimal("1000.00"),
                LocalDateTime.now()
        );

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(bankAccount));

        AccountResponse response = accountService.getAccount(accountId);

        assertThat(response).isNotEqualTo(null);
        assertThat(response.getId()).isEqualTo(accountId);
        assertThat(response.getClientName()).isEqualTo("Test");
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        Long nonExistentId = -1L;
        when(accountRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.getAccount(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account not found with id: " + nonExistentId);
    }
}
