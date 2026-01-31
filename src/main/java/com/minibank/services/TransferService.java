package com.minibank.services;

import com.minibank.models.BankAccount;
import com.minibank.models.Transaction;
import com.minibank.models.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TransferService {
    private final AtomicLong nextTransactionId = new AtomicLong(1L);

    public Transaction transfer(BankAccount fromAccount,
                                BankAccount toAccount,
                                BigDecimal amount,
                                String comment) {
        if (Objects.equals(fromAccount.getId(), toAccount.getId())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        return new Transaction(
                nextTransactionId.getAndIncrement(),
                fromAccount.getId(),
                toAccount.getId(),
                amount,
                TransactionStatus.SUCCESS,
                LocalDateTime.now(),
                comment
        );
    }
}
