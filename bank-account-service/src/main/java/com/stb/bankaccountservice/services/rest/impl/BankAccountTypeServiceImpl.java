package com.stb.bankaccountservice.services.rest.impl;

import com.stb.bankaccountservice.dtos.BankAccountTypePayloadDTO;
import com.stb.bankaccountservice.entities.BankAccountType;
import com.stb.bankaccountservice.repositories.BankAccountTypeRepository;
import com.stb.bankaccountservice.services.rest.BankAccountTypeService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountTypeServiceImpl implements BankAccountTypeService {
    private final BankAccountTypeRepository bankAccountTypeRepository;

    @Autowired
    public BankAccountTypeServiceImpl(BankAccountTypeRepository bankAccountTypeRepository) {
        this.bankAccountTypeRepository = bankAccountTypeRepository;
    }
    @Override
    public BankAccountType create(BankAccountTypePayloadDTO createBankAccountTypeDTO) {
        BankAccountType bankAccountType = this.bankAccountTypeRepository.findByName(createBankAccountTypeDTO.getName())
                .orElse(null);
        if (bankAccountType != null) {
            return bankAccountType;
        }

        BankAccountType newBankAccountType = BankAccountType.builder()
                .name(createBankAccountTypeDTO.getName())
                .transactionLimit(createBankAccountTypeDTO.getTransactionLimit())
                .unlimited(createBankAccountTypeDTO.isUnlimited())
                .build();

        return bankAccountTypeRepository.save(newBankAccountType);
    }

    @Override
    public BankAccountType update(Long id, BankAccountTypePayloadDTO updateBankAccountTypeDTO) {
        BankAccountType bankAccountType = this.get(id);

        bankAccountType.setName(updateBankAccountTypeDTO.getName());
        bankAccountType.setTransactionLimit(updateBankAccountTypeDTO.getTransactionLimit());
        bankAccountType.setUnlimited(updateBankAccountTypeDTO.isUnlimited());
        return bankAccountTypeRepository.save(bankAccountType);
    }

    @Override
    public BankAccountType get(Long id) {
        return bankAccountTypeRepository.findById(id).
                orElseThrow( () -> new EntityNotFoundException("BankAccountType not found"));
    }

    @Override
    public BankAccountType getByName(String name) {
        return (BankAccountType) bankAccountTypeRepository.findByName(name).
                orElseThrow(() -> new EntityNotFoundException("BankAccountType not found"));
    }

    @Override
    public List<BankAccountType> list() {
        return bankAccountTypeRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        this.bankAccountTypeRepository.deleteById(id);
    }
}
