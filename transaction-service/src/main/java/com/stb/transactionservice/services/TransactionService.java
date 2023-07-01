package com.stb.transactionservice.services;

import com.stb.transactionservice.dtos.CreateTransactionDTO;
import com.stb.transactionservice.entities.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction create(CreateTransactionDTO createTransactionDTO);
    List<Transaction> list();
    List<Transaction> findByBankAccountId(Long bankAccountId);
    Transaction get(String id);
}
