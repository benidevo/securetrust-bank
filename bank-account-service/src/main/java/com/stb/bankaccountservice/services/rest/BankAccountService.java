package com.stb.bankaccountservice.services.rest;

import com.stb.bankaccountservice.dtos.CreateBankAccountDTO;
import com.stb.bankaccountservice.dtos.UpdateBankAccountDTO;
import com.stb.bankaccountservice.entities.BankAccount;

import java.util.List;

public interface BankAccountService {
    BankAccount create(CreateBankAccountDTO createBankAccountDTO);

    BankAccount update(Long id, UpdateBankAccountDTO updateBankAccountDTO);

    BankAccount get(Long id);

    BankAccount getByAccountNumber(String accountNumber);

    BankAccount getByUserId(Long userId);

    List<BankAccount> list();

    void delete(Long id);
}
