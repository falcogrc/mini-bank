package com.minibank.models;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

public class BankAccountTest {
    @Test
    void shouldCreateAccountWithCorrectValues() {
        // given
        Long id = 1L;
        String clientName = "Ivan Ivanov";
        BigDecimal balance = new BigDecimal("1000.00");

        // when
        BankAccount account = new BankAccount(id, clientName, balance);

        // then
        assertThat(account.getId()).isEqualTo(id);
        assertThat(account.getClientName()).isEqualTo(clientName);
        assertThat(account.getBalance()).isEqualByComparingTo(balance);
    }

    @Test
    void shouldIncreaseBalanceWhenDepositPositiveAmount() {
        // given
        BankAccount account = new BankAccount(1L, "Test", new BigDecimal("1000.00"));
        BigDecimal depositAmount = new BigDecimal("500.00");

        // when
        account.deposit(depositAmount);

        // then
        assertThat(account.getBalance()).isEqualByComparingTo("1500.00");
    }

    @Test
    void shouldDecreaseBalanceWhenWithdrawValidAmount() {
        // given
        BankAccount account = new BankAccount(1L, "test", new BigDecimal("1000.00"));

        // when
        account.withdraw(new BigDecimal("300.00"));

        // then
        assertThat(account.getBalance()).isEqualByComparingTo("700.00");
    }

    @Test
    void shouldThrowExceptionWhenWithdrawMoreThanBalance() {
        // given
        BankAccount account = new BankAccount(1L, "test", new BigDecimal("100.00"));

        // when & then
        assertThatThrownBy(() -> account.withdraw(new BigDecimal("200.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("insufficient funds");
    }


    @Test
    void shouldThrowExceptionWhenDepositNullAmount() {
        BankAccount account = new BankAccount(1L, "Test", new BigDecimal("1000.00"));

        assertThatThrownBy(() -> account.deposit(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenDepositNegativeAmount() {
        BankAccount account = new BankAccount(1L, "Test", new BigDecimal("1000.00"));

        assertThatThrownBy(() -> account.deposit(new BigDecimal("-100.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be positive");
    }

    @Test
    void shouldThrowExceptionWhenWithdrawNullAmount() {
        // given
        BankAccount account = new BankAccount(1L, "Test", new BigDecimal("1000.00"));

        // when & then
        assertThatThrownBy(() -> account.withdraw(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null");

    }

    @Test
    void shouldThrowExceptionWhenWithdrawZeroAmount() {
        // given
        BankAccount account = new BankAccount(1L, "Test", new BigDecimal("10.00"));

        // when & then
        assertThatThrownBy(() -> account.withdraw(new BigDecimal("0.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Withdrawal amount must be positive");
    }
}
