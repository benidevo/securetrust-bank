package com.stb.bankaccountservice.controllers;

import com.stb.bankaccountservice.common.annotation.HandleValidationErrors;
import com.stb.bankaccountservice.dtos.CreateBankAccountDTO;
import com.stb.bankaccountservice.dtos.UpdateBankAccountDTO;
import com.stb.bankaccountservice.entities.BankAccount;
import com.stb.bankaccountservice.services.rest.BankAccountService;
import com.stb.bankaccountservice.utils.apiResponse.ApiResponse;
import com.stb.bankaccountservice.utils.apiResponse.BankAccountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Bank Account")
@RestController
@RequestMapping("/api/v1/accounts")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Operation(description = "Create a new bank account")
    @PostMapping
    @HandleValidationErrors
    public ResponseEntity<BankAccountResponse<BankAccount>> create(@Valid @RequestBody
                                                                       CreateBankAccountDTO createBankAccountDTO,
                                                                   BindingResult result) {
        BankAccount bankAccount = bankAccountService.create(createBankAccountDTO);
        BankAccountResponse<BankAccount> response = BankAccountResponse.builder()
                .success(true)
                .message("Bank account created successfully")
                .data(bankAccount)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(description = "List all bank account types")
    @GetMapping
    public ResponseEntity<ApiResponse> list() {
        List<BankAccount> bankAccounts = bankAccountService.list();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Bank accounts retrieved successfully")
                .data(bankAccounts)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Get a bank account type by id")
    @GetMapping("/{id}")
    public ResponseEntity<BankAccountResponse<BankAccount>> get(@PathVariable Long id) {
        BankAccount bankAccount = bankAccountService.get(id);
        BankAccountResponse<BankAccount> response = BankAccountResponse.builder()
                .success(true)
                .message("Bank account retrieved successfully")
                .data(bankAccount)
                .build();

        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Update a bank account")
    @PutMapping("/{id}")
    @HandleValidationErrors
    public ResponseEntity<BankAccountResponse<BankAccount>> update(@PathVariable Long id,
                                                                   @Valid @RequestBody
                                                                   UpdateBankAccountDTO updateBankAccountDTO,
                                                                   BindingResult result) {
        BankAccount bankAccount = bankAccountService.update(id, updateBankAccountDTO);
        BankAccountResponse<BankAccount> response = BankAccountResponse.builder()
                .success(true)
                .message("Bank account updated successfully")
                .data(bankAccount)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @Operation(description = "Delete a bank account")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bankAccountService.delete(id);
    }
}
