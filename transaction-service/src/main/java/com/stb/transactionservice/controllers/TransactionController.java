package com.stb.transactionservice.controllers;

import com.stb.transactionservice.dtos.CreateTransactionDTO;
import com.stb.transactionservice.entities.Transaction;
import com.stb.transactionservice.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Transactions")
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Operation(description = "Create a new transaction")
    @PostMapping
    public Transaction create(@Valid @RequestBody CreateTransactionDTO transaction) {
        return transactionService.create(transaction);
    }

    @Operation(description = "List all transactions")
    @GetMapping
    public List<Transaction> list() {
        return transactionService.list();
    }

    @Operation(description = "List all transactions by bank account id")
    @GetMapping("/account/{bankAccountId}")
    public List<Transaction> findByBankAccountId(@PathVariable Long bankAccountId){
        return transactionService.findByBankAccountId(bankAccountId);
    }

    @Operation(description = "Get a transaction by id")
    @GetMapping("/{id}")
    public Transaction get(@PathVariable String id){
        return transactionService.get(id);
    }
}
