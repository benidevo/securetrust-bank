package com.stb.bankaccountservice.services;

import com.stb.bankaccountservice.dtos.CreateBankAccountDTO;
import com.stb.bankaccountservice.entities.BankAccount;

import java.util.List;

public interface BankAccountService {
    BankAccount create(CreateBankAccountDTO createBankAccountDTO);

    BankAccount update(BankAccount bankAccount);

    BankAccount get(Long id);

    BankAccount getByAccountNumber(String accountNumber);

    BankAccount getByUserId(Long userId);

    List<BankAccount> list();

    void delete(Long id);
}
