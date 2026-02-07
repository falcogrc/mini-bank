package com.minibank.controllers;

import com.minibank.dto.TransferRequest;
import com.minibank.services.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    private final TransferService transferService;


    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferRequest request) {
        // TODO: реализовать позже
        return ResponseEntity.ok("Transfer endpoint works!");
    }
}
