package com.stb.bankaccountservice.services.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stb.bankaccountservice.dtos.CreateBankAccountDTO;
import com.stb.bankaccountservice.dtos.SetTransactionPinDTO;
import com.stb.bankaccountservice.dtos.UpdateBankAccountDTO;
import com.stb.bankaccountservice.dtos.UpdateTransactionPinDTO;
import com.stb.bankaccountservice.entities.BankAccount;

import java.util.List;

public interface BankAccountService {
    BankAccount create(CreateBankAccountDTO createBankAccountDTO) throws JsonProcessingException;

    BankAccount update(Long id, UpdateBankAccountDTO updateBankAccountDTO);

    BankAccount get(Long id);

    BankAccount getByAccountNumber(String accountNumber);

    BankAccount getByUserId(Long userId);

    List<BankAccount> list();

    void delete(Long id);

    void setTransactionPin(Long bankAccountId, SetTransactionPinDTO setTransactionPinDTO);

    void changeTransactionPin(Long id, UpdateTransactionPinDTO updateTransactionPinDTO);
}
