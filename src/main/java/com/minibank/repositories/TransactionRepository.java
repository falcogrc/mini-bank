package com.minibank.repositories;

import com.minibank.models.Transaction;

import java.util.HashMap;
import java.util.Map;

public class TransactionRepository {
    private final Map<Long, Transaction> transactions = new HashMap<>();
    private Long nextId = 1L;

    public Transaction save(Transaction transaction) {
        if (transaction.getId() == null) {
            Transaction newTransaction = new Transaction(
                    nextId++,
                    transaction.getFromAccountId(),
                    transaction.getToAccountId(),
                    transaction.getAmount(),
                    transaction.getStatus(),
                    transaction.getTimestamp(),
                    transaction.getComment()
            );
            transactions.put(newTransaction.getId(), newTransaction);
            return newTransaction;
        } else {
            transactions.put(transaction.getId(), transaction);
            return transaction;
        }
    }
}
