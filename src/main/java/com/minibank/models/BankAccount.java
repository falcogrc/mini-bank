package com.minibank.models;

import com.minibank.exeptions.AccountNotFoundException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class BankAccount {
    @Setter
    private Long id;

    @Setter
    private String clientName;

    private BigDecimal balance;

    @Setter
    private LocalDateTime createdAt;

    public BankAccount(Long id, String clientName, BigDecimal balance, LocalDateTime createdAt) {
        this.id = id;
        this.clientName = clientName;
        this.balance = balance;
        this.createdAt = createdAt;
    }
    public BankAccount(String clientName, BigDecimal balance) {
        this(null, clientName, balance, LocalDateTime.now());
    }

    public void deposit(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
       if (amount == null) {
           throw new IllegalArgumentException("Amount cannot be null");
       }
       if (amount.compareTo(BigDecimal.ZERO) <= 0) {
           throw new IllegalArgumentException("Withdrawal amount must be positive");
       }
       if (this.balance.compareTo(amount) < 0) {
           throw new IllegalArgumentException("insufficient funds");
       }
       this.balance = this.balance.subtract(amount);
    }
}
