package com.stb.transactionservice.controllers;

import com.stb.transactionservice.common.annotation.HandleValidationErrors;
import com.stb.transactionservice.dtos.CreateTransactionDTO;
import com.stb.transactionservice.entities.Transaction;
import com.stb.transactionservice.services.TransactionService;
import com.stb.transactionservice.utils.apiResponse.TransactionResponse;
import com.stb.transactionservice.utils.apiResponse.TransactionsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.stb.transactionservice.utils.Constants.ADMIN_ROLE;

@Tag(name = "Transactions")
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Operation(description = "Create a new transaction")
    @PostMapping
    @HandleValidationErrors
    public ResponseEntity<TransactionResponse<Transaction>> create(@Valid @RequestBody
                                                                       CreateTransactionDTO transaction,
                                                                   BindingResult result) {
        Transaction newTransaction = transactionService.create(transaction);
        TransactionResponse<Transaction> response = TransactionResponse.builder()
                .success(true)
                .message("Transaction successful")
                .data(newTransaction)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(description = "List all transactions")
    @GetMapping
    @PreAuthorize("hasAuthority('" + ADMIN_ROLE + "')")
    public ResponseEntity<TransactionsResponse<List<Transaction>>> list() {
        List<Transaction> transactions = transactionService.list();
        TransactionsResponse<List<Transaction> >response = TransactionsResponse.builder()
                .success(true)
                .message("Transactions retrieved successfully")
                .data(transactions)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "List all transactions by bank account id")
    @GetMapping("/account/{bankAccountId}")
    public ResponseEntity<TransactionsResponse<List<Transaction>>> findByBankAccountId(@PathVariable Long bankAccountId){
        List<Transaction> transactions = transactionService.findByBankAccountId(bankAccountId);
        TransactionsResponse<List<Transaction>> response = TransactionsResponse.builder()
                .success(true)
                .message("Transactions retrieved successfully")
                .data(transactions)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Get a transaction by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + ADMIN_ROLE + "')")
    public ResponseEntity<TransactionResponse<Transaction>> get(@PathVariable String id){
        Transaction transaction = transactionService.get(id);
        TransactionResponse<Transaction> response = TransactionResponse.builder()
                .success(true)
                .message("Transaction retrieved successfully")
                .data(transaction)
                .build();

        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
}
