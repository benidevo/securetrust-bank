package com.stb.transactionservice.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stb.transactionservice.common.exception.BadRequestException;
import com.stb.transactionservice.dtos.CreateTransactionDTO;
import com.stb.transactionservice.dtos.TransactionParty;
import com.stb.transactionservice.entities.Transaction;
import com.stb.transactionservice.repositories.TransactionRepository;
import com.stb.transactionservice.services.TransactionService;
import com.stb.transactionservice.utils.BankAccount;
import com.stb.transactionservice.utils.BankAccountType;
import com.stb.transactionservice.utils.httpClient.HttpClientHelper;
import com.stb.transactionservice.utils.TransactionType;
import com.stb.transactionservice.utils.httpClient.responses.BankAccountResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;
    private HttpClientHelper client;
    private HttpServletRequest request;
    private ObjectMapper objectMapper;
    @Autowired
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            HttpClientHelper client,
            HttpServletRequest request,
            ObjectMapper objectMapper
    ) {
        this.transactionRepository = transactionRepository;
        this.client = client;
        this.request = request;
        this.objectMapper = objectMapper;
    }

    @Override
    public Transaction create(Long bankAccountId, CreateTransactionDTO createTransactionDTO)
            throws JsonProcessingException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);

        client.setAuthorizationHeader(token);
        Mono<String> senderRes = client.get("http://bank-account:8080/api/v1/accounts/" + bankAccountId);
        String senderJsonString;
        try {
            senderJsonString = senderRes.block();
        } catch  (Exception ex ) {
            throw new BadRequestException("Bank account not found");
        }

        BankAccountResponse senderResponse = objectMapper.readValue(senderJsonString, BankAccountResponse.class);
        BankAccount senderBankAccount = senderResponse.getData();
        BankAccountType senderBankAccountType = senderBankAccount.getAccountType();

        if (!senderBankAccount.getIsActive()) {
            throw new BadRequestException("Bank account is not active");
        }
        if (!senderBankAccountType.getUnlimited()) {
            if (senderBankAccountType.getTransactionLimit().compareTo(createTransactionDTO.getAmount()) < 0) {
                throw new BadRequestException("Upgrade your account to complete this transaction");
            }
        }

        if (senderBankAccount.getBalance().compareTo(createTransactionDTO.getAmount()) < 0) {
            throw new BadRequestException("Insufficient balance");
        }

        Mono<String> recipientRes = client.get("http://bank-account:8080/api/v1/accounts/number/" +
                createTransactionDTO
                        .getTransactionParty()
                        .getNumber());
        String recipientJsonString;
        try {
            recipientJsonString = recipientRes.block();
        } catch (Exception ex) {
            throw new BadRequestException("Recipient account not found");
        }

        BankAccountResponse recipientResponse = objectMapper.readValue(
                recipientJsonString,
                BankAccountResponse.class
        );

        BankAccount recipientBankAccount = recipientResponse.getData();

        createTransactionDTO.getTransactionParty().setName(recipientBankAccount.getName());
        Transaction senderTransaction = Transaction.builder()
                .bankAccountId(bankAccountId)
                .type(createTransactionDTO.getType())
                .amount(createTransactionDTO.getAmount())
                .description(createTransactionDTO.getDescription())
                .transactionParty(createTransactionDTO.getTransactionParty())
                .build();

        TransactionParty recipientTransactionParty = TransactionParty.builder()
                .number(senderBankAccount.getNumber())
                .name(senderBankAccount.getName())
                .build();

        Transaction recipientTransaction = Transaction.builder()
                .bankAccountId(recipientBankAccount.getId())
                .type(TransactionType.valueOf("CREDIT"))
                .amount(createTransactionDTO.getAmount())
                .description(createTransactionDTO.getDescription())
                .transactionParty(recipientTransactionParty)
                .build();
        transactionRepository.save(recipientTransaction);

        return transactionRepository.save(senderTransaction);
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
