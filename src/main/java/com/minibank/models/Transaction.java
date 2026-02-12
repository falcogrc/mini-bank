package com.minibank.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Transaction {
    private final Long id;

    private final Long fromAccountId;

    private final Long toAccountId;

    @Setter
    private BigDecimal amount;

    private final TransactionStatus status;

    @Setter
    private LocalDateTime timestamp;

    @Setter
    private String comment;

    public Transaction(Long id,
                       Long fromAccountId,
                       Long toAccountId,
                       BigDecimal amount,
                       TransactionStatus status,
                       LocalDateTime timestamp,
                       String comment) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = status;
        this.timestamp = timestamp;
        this.comment = comment;
    }
}
