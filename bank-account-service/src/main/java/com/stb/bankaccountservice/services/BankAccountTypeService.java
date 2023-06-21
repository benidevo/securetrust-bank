package com.stb.bankaccountservice.services;

import com.stb.bankaccountservice.dtos.CreateBankAccountTypeDTO;
import com.stb.bankaccountservice.entities.BankAccountType;

import java.util.List;

public interface BankAccountTypeService {
    BankAccountType create(CreateBankAccountTypeDTO createBankAccountTypeDTO);

    BankAccountType update(BankAccountType bankAccountType);

    BankAccountType get(Long id);

    BankAccountType getByName(String name);

    List<BankAccountType> list();

    void delete(Long id);
}
