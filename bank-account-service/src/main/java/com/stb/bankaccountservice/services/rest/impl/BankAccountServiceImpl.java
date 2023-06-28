package com.stb.bankaccountservice.services.rest.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stb.bankaccountservice.dtos.CreateBankAccountDTO;
import com.stb.bankaccountservice.dtos.UpdateBankAccountDTO;
import com.stb.bankaccountservice.entities.BankAccount;
import com.stb.bankaccountservice.entities.BankAccountType;
import com.stb.bankaccountservice.rabbitMQ.RabbitMQProducer;
import com.stb.bankaccountservice.repositories.BankAccountRepository;
import com.stb.bankaccountservice.repositories.BankAccountTypeRepository;
import com.stb.bankaccountservice.services.rest.BankAccountService;
import com.stb.bankaccountservice.utils.AccountNumberGenerator;
import com.stb.bankaccountservice.utils.BankAccountNotification;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.stb.bankaccountservice.utils.Constants.*;

@Slf4j
@Service
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final BankAccountTypeRepository bankAccountTypeRepository;
    private final RabbitMQProducer rabbitMQProducer;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository,
                                  AccountNumberGenerator accountNumberGenerator,
            BankAccountTypeRepository bankAccountTypeRepository,
            RabbitMQProducer rabbitMQProducer) {
        this.bankAccountRepository = bankAccountRepository;
        this.accountNumberGenerator = accountNumberGenerator;
        this.bankAccountTypeRepository = bankAccountTypeRepository;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @Override
    public BankAccount create(CreateBankAccountDTO createBankAccountDTO) throws JsonProcessingException {
        Optional<Object> existingBankAccount = bankAccountRepository.findByUserId(createBankAccountDTO.getUserId());
        if (existingBankAccount.isPresent()) {
            throw new EntityExistsException("This user already has a bank account");
        }

        Optional<BankAccountType> bankAccountType = bankAccountTypeRepository.findByName(BASIC_ACCOUNT_TYPE);
        if (bankAccountType.isEmpty()) {
            BankAccountType basicAccountType = BankAccountType.builder()
                    .name(BASIC_ACCOUNT_TYPE)
                    .transactionLimit(BASIC_ACCOUNT_TYPE_TRANSACTION_LIMIT)
                    .unlimited(false)
                    .build();
            bankAccountType = Optional.of(bankAccountTypeRepository.save(basicAccountType));
        }
        String accountNumber = accountNumberGenerator.generateAccountNumber(createBankAccountDTO.getName());

        BankAccount bankAccount = BankAccount.builder()
                .name(createBankAccountDTO.getName())
                .userId(createBankAccountDTO.getUserId())
                .number(accountNumber)
                .accountType(bankAccountType.get())
                .balance(BigDecimal.valueOf(0.0))
                .isActive(true)
                .build();
        bankAccount = bankAccountRepository.save(bankAccount);

        ObjectMapper objectMapper = new ObjectMapper();
        BankAccountNotification bankAccountNotification = BankAccountNotification.builder()
                .email(createBankAccountDTO.getEmail())
                .number(bankAccount.getNumber())
                .name(bankAccount.getName())
                .build();
        String message = objectMapper.writeValueAsString(bankAccountNotification);

        if (message != null)
            rabbitMQProducer.publishMessage(NEW_BANK_ACCOUNT_NOTIFICATION_QUEUE, message);

        return bankAccount;
    }

    @Override
    public BankAccount update(Long id, UpdateBankAccountDTO updateBankAccountDTO) {
        BankAccount bankAccount = this.get(id);
        bankAccount.setName(updateBankAccountDTO.getName());
        bankAccount.setIsActive(updateBankAccountDTO.isActive());

        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount get(Long id) {
        return (BankAccount) bankAccountRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("BankAccount not found")
        );
    }

    @Override
    public BankAccount getByAccountNumber(String accountNumber) {
        return (BankAccount) bankAccountRepository.findByNumber(accountNumber).orElseThrow(
            () -> new EntityNotFoundException("BankAccount not found")
        );
    }

    @Override
    public BankAccount getByUserId(Long userId) {
        return (BankAccount) bankAccountRepository.findByUserId(userId).orElseThrow(
            () -> new EntityNotFoundException("BankAccount not found")
        );
    }

    @Override
    public List<BankAccount> list() {
        return bankAccountRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        bankAccountRepository.deleteById(id);
    }
}
