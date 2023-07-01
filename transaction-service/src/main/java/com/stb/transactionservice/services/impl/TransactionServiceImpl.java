package com.stb.transactionservice.services.impl;

import com.stb.transactionservice.dtos.CreateTransactionDTO;
import com.stb.transactionservice.entities.Transaction;
import com.stb.transactionservice.repositories.TransactionRepository;
import com.stb.transactionservice.services.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public Transaction create(CreateTransactionDTO createTransactionDTO) {
        Transaction newTransaction = Transaction.builder()
                .bankAccountId(createTransactionDTO.getBankAccountId())
                .isInternal(createTransactionDTO.isInternal())
                .type(createTransactionDTO.getType())
                .amount(createTransactionDTO.getAmount())
                .description(createTransactionDTO.getDescription())
                .beneficiary(createTransactionDTO.getBeneficiary())
                .build();

        return transactionRepository.save(newTransaction);
    }

    @Override
    public List<Transaction> list() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> findByBankAccountId(Long bankAccountId) {
        return transactionRepository.findByBankAccountId(bankAccountId);
    }

    @Override
    public Transaction get(String id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
    }
}
