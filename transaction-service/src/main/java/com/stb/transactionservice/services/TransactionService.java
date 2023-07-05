package com.stb.transactionservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stb.transactionservice.dtos.CreateTransactionDTO;
import com.stb.transactionservice.entities.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction create(Long bankAccountId, CreateTransactionDTO createTransactionDTO) throws JsonProcessingException;
    List<Transaction> list();
    List<Transaction> findByBankAccountId(Long bankAccountId);
    Transaction get(String id);
}
