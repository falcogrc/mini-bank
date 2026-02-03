package com.minibank.repositories;

import com.minibank.models.BankAccount;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryAccountRepository implements AccountRepository {
    private final Map<Long, BankAccount> accounts = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Optional<BankAccount> findById(Long id) {
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public BankAccount save(BankAccount account) {
        if (account.getId() == null) {
            account.setId(nextId++);
            if (account.getCreatedAt() == null) {
                account.setCreatedAt(LocalDateTime.now());
            }
        }
        accounts.put(account.getId(), account);
        return account;
    }
}
