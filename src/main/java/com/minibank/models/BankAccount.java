package com.minibank.models;

import lombok.Setter;

import java.math.BigDecimal;

public class BankAccount {
    @Setter
    private Long id;

    @Setter
    private String clientName;

    private BigDecimal balance;

    public BankAccount(Long id, String clientName, BigDecimal balance) {
        this.id = id;
        this.clientName = clientName;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getClientName() {
        return clientName;
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
