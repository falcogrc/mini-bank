package com.minibank.repositories;

import com.minibank.models.BankAccount;

import java.util.Optional;


public interface AccountRepository {
    Optional<BankAccount> findById(Long id);
    BankAccount save(BankAccount account);
}
