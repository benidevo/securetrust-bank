package com.stb.bankaccountservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stb.bankaccountservice.common.annotation.HandleValidationErrors;
import com.stb.bankaccountservice.dtos.CreateBankAccountDTO;
import com.stb.bankaccountservice.dtos.SetTransactionPinDTO;
import com.stb.bankaccountservice.dtos.UpdateBankAccountDTO;
import com.stb.bankaccountservice.dtos.UpdateTransactionPinDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.stb.bankaccountservice.utils.Constants.ADMIN_ROLE;

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
    @PreAuthorize("hasAuthority('" + ADMIN_ROLE + "')")
    @HandleValidationErrors
    public ResponseEntity<BankAccountResponse<BankAccount>> create(@Valid @RequestBody
                                                                       CreateBankAccountDTO createBankAccountDTO,
            BindingResult result) throws JsonProcessingException {
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
    @PreAuthorize("hasAuthority('" + ADMIN_ROLE + "')")
    public ResponseEntity<ApiResponse> list() {
        List<BankAccount> bankAccounts = bankAccountService.list();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Bank accounts retrieved successfully")
                .data(bankAccounts)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Get bank account of the current authenticated user")
    @GetMapping("/me")
    public ResponseEntity<BankAccountResponse<BankAccount>> getAuthUserBankAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        BankAccount bankAccount = bankAccountService.getByUserId(userId);
        BankAccountResponse<BankAccount> response = BankAccountResponse.builder()
                .success(true)
                .message("Bank account retrieved successfully")
                .data(bankAccount)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Get a bank account type by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + ADMIN_ROLE + "')")
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
    @PreAuthorize("hasAuthority('" + ADMIN_ROLE + "')")
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
    @PreAuthorize("hasAuthority('" + ADMIN_ROLE + "')")
    public void delete(@PathVariable Long id) {
        bankAccountService.delete(id);
    }

    @Operation(description = "Set transaction pin")
    @PutMapping("/{id}/pin")
    @HandleValidationErrors
    public ResponseEntity<ApiResponse> setTransactionPin(@Valid @RequestBody
                                                         SetTransactionPinDTO setTransactionPinDTO,
                                                         BindingResult result, @PathVariable Long id) {
        this.bankAccountService.setTransactionPin(id, setTransactionPinDTO);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Transaction pin updated successfully")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Change transaction pin")
    @PatchMapping("/{id}/pin")
    @HandleValidationErrors
    public ResponseEntity<ApiResponse> changeTransactionPin(@Valid @RequestBody
                                                                UpdateTransactionPinDTO updateTransactionPinDTO,
                                                            BindingResult result, @PathVariable Long id) {
        this.bankAccountService.changeTransactionPin(id, updateTransactionPinDTO);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Transaction pin updated successfully")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
