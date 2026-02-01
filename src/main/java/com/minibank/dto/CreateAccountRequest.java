package com.minibank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateAccountRequest {

    @NotBlank(message = "Client name is required")
    private String clientName;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Initial balance must be positive")
    private BigDecimal initialBalance;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
