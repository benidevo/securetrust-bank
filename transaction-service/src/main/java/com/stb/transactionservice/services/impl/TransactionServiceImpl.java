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
    public Transaction create(Long bankAccountId, CreateTransactionDTO createTransactionDTO) {
        // check if user's bank account can perform the transaction
        // check account balance
        // check if recipient account exists
        // make transaction
        Transaction newTransaction = Transaction.builder()
                .bankAccountId(bankAccountId)
                .isInternal(createTransactionDTO.isInternal())
                .type(createTransactionDTO.getType())
                .amount(createTransactionDTO.getAmount())
                .description(createTransactionDTO.getDescription())
                .transactionParty(createTransactionDTO.getTransactionParty())
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
