package com.minibank.services;

import com.minibank.dto.AccountResponse;
import com.minibank.dto.CreateAccountRequest;
import com.minibank.models.BankAccount;
import com.minibank.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponse createAccount(CreateAccountRequest request) {
        BankAccount bankAccount = new BankAccount(
                null,
                request.getClientName(),
                request.getInitialBalance(),
                LocalDateTime.now()
        );
        BankAccount savedAccount = accountRepository.save(bankAccount);

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getClientName(),
                savedAccount.getBalance(),
                LocalDateTime.now()
        );
    }

    public AccountResponse getAccount(Long id) {
        BankAccount account = accountRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + id));

        return new AccountResponse(
                account.getId(),
                account.getClientName(),
                account.getBalance(),
                null // createdAt пока не храню
        );
    }
}
