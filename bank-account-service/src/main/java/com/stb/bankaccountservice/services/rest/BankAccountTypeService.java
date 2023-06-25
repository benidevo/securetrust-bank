package com.stb.bankaccountservice.services.rest;

import com.stb.bankaccountservice.dtos.BankAccountTypePayloadDTO;
import com.stb.bankaccountservice.entities.BankAccountType;

import java.util.List;

public interface BankAccountTypeService {
    BankAccountType create(BankAccountTypePayloadDTO createBankAccountTypeDTO);

    BankAccountType update(Long id, BankAccountTypePayloadDTO updateBankAccountTypeDTO);

    BankAccountType get(Long id);

    BankAccountType getByName(String name);

    List<BankAccountType> list();

    void delete(Long id);
}
