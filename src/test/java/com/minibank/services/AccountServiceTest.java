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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        // TODO: напиши тест для метода getAccount
        // Подсказка: используй when(...).thenReturn(...)
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        // TODO: напиши тест для случая, когда счет не найден
        // Подсказка: используй assertThatThrownBy
    }
}
