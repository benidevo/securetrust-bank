package com.stb.bankaccountservice.controllers;

import com.stb.bankaccountservice.common.annotation.HandleValidationErrors;
import com.stb.bankaccountservice.dtos.BankAccountTypePayloadDTO;
import com.stb.bankaccountservice.entities.BankAccountType;
import com.stb.bankaccountservice.services.rest.BankAccountTypeService;
import com.stb.bankaccountservice.utils.apiResponse.ApiResponse;
import com.stb.bankaccountservice.utils.apiResponse.BankAccountTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Bank Account Type")
@RestController
@RequestMapping("/api/v1/account-types")
public class BankAccountTypeController {
    private final BankAccountTypeService bankAccountTypeService;

    @Autowired
    public BankAccountTypeController(BankAccountTypeService bankAccountTypeService) {
        this.bankAccountTypeService = bankAccountTypeService;
    }

    @Operation(description = "Create a new bank account type")
    @PostMapping
    @HandleValidationErrors
    public ResponseEntity<BankAccountTypeResponse<BankAccountType>> create(@Valid @RequestBody
                                                                               BankAccountTypePayloadDTO
                                                                                       createBankAccountTypeDTO,
                                                                           BindingResult result) {
        BankAccountType bankAccountType = bankAccountTypeService.create(createBankAccountTypeDTO);
        BankAccountTypeResponse<BankAccountType> response = BankAccountTypeResponse.builder()
                .success(true)
                .message("Bank account type created successfully")
                .data(bankAccountType)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(description = "List all bank account types")
    @GetMapping
    public ResponseEntity<ApiResponse> list() {
        List<BankAccountType> bankAccountTypes = bankAccountTypeService.list();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Bank account types retrieved successfully")
                .data(bankAccountTypes)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Get a bank account type by id")
    @GetMapping("/{id}")
    public ResponseEntity<BankAccountTypeResponse<BankAccountType>> get(@PathVariable Long id) {
        BankAccountType bankAccountType = bankAccountTypeService.get(id);
        BankAccountTypeResponse<BankAccountType> response = BankAccountTypeResponse.builder()
                .success(true)
                .message("Bank account type retrieved successfully")
                .data(bankAccountType)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Update a bank account type")
    @PutMapping("/{id}")
    @HandleValidationErrors
    public ResponseEntity<BankAccountTypeResponse<BankAccountType>> update(@PathVariable Long id,
                                  @Valid @RequestBody BankAccountTypePayloadDTO updateBankAccountTypeDTO,
                                  BindingResult result) {
        BankAccountType bankAccountType = bankAccountTypeService.update(id, updateBankAccountTypeDTO);
        BankAccountTypeResponse<BankAccountType> response = BankAccountTypeResponse.builder()
                .success(true)
                .message("Bank account type updated successfully")
                .data(bankAccountType)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Delete a bank account type")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bankAccountTypeService.delete(id);
    }
}
