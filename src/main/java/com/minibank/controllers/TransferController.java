package com.minibank.controllers;

import com.minibank.dto.TransferRequest;
import com.minibank.exeptions.AccountNotFoundException;
import com.minibank.models.BankAccount;
import com.minibank.models.Transaction;
import com.minibank.repositories.TransactionRepository;
import com.minibank.services.AccountService;
import com.minibank.services.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    private final TransferService transferService;

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;


    public TransferController(TransferService transferService,
                              AccountService accountService,
                              TransactionRepository transactionRepository) {
        this.transferService = transferService;
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest request) {
        try {
            BankAccount fromAccount = accountService.getAccountEntity(request.getFromAccountId());
            BankAccount toAccount = accountService.getAccountEntity(request.getToAccountId());

            Transaction transaction = transferService.transfer(
                    fromAccount,
                    toAccount,
                    request.getAmount(),
                    request.getComment()
            );

            transactionRepository.save(transaction);

            return ResponseEntity.ok(transaction);
        } catch (AccountNotFoundException e) {
            Map<String, Object> error = Map.of(
                    "error", "Account not found",
                    "message", e.getMessage(),
                    "timestamp", LocalDateTime.now(),
                    "status", 404
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = Map.of(
                    "error", "Transfer failed",
                    "message", e.getMessage(),
                    "timestamp", LocalDateTime.now(),
                    "status", 400
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
