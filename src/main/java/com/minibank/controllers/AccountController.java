package com.minibank.controllers;

import com.minibank.dto.AccountResponse;
import com.minibank.dto.CreateAccountRequest;
import com.minibank.exeptions.AccountNotFoundException;
import com.minibank.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) {
        try {
            AccountResponse response = accountService.getAccount(id);
            return ResponseEntity.ok(response);
        } catch (AccountNotFoundException e) {
            Map<String, Object> error = Map.of(
                    "error", "Account not found",
                    "message", e.getMessage(),
                    "timestamp", LocalDateTime.now(),
                    "status", 404
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

}
