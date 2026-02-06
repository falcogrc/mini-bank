package com.minibank.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter

public class AccountResponse {
    private Long id;
    private String clientName;
    private BigDecimal balance;
    private LocalDateTime createdAt;

    public AccountResponse(Long id, String clientName, BigDecimal balance, LocalDateTime createdAt) {
        this.id = id;
        this.clientName = clientName;
        this.balance = balance;
        this.createdAt = createdAt;
    }

}
