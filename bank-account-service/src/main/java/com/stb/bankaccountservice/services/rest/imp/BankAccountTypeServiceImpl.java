package com.stb.bankaccountservice.services.rest.imp;

import com.stb.bankaccountservice.dtos.CreateBankAccountTypeDTO;
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
    public BankAccountType create(CreateBankAccountTypeDTO createBankAccountTypeDTO) {
        BankAccountType bankAccountType = BankAccountType.builder()
                .name(createBankAccountTypeDTO.getName())
                .transactionLimit(createBankAccountTypeDTO.getTransactionLimit())
                .build();
        return bankAccountTypeRepository.save(bankAccountType);
    }

    @Override
    public BankAccountType update(BankAccountType bankAccountType) {
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
