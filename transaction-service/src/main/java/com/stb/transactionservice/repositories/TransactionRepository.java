package com.stb.transactionservice.repositories;

import com.stb.transactionservice.entities.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByBankAccountId(Long bankAccountId);
}
