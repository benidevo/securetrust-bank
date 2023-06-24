package com.stb.bankaccountservice.services.rest.imp;

import com.stb.bankaccountservice.dtos.CreateBankAccountDTO;
import com.stb.bankaccountservice.entities.BankAccount;
import com.stb.bankaccountservice.entities.BankAccountType;
import com.stb.bankaccountservice.repositories.BankAccountRepository;
import com.stb.bankaccountservice.repositories.BankAccountTypeRepository;
import com.stb.bankaccountservice.services.rest.BankAccountService;
import com.stb.bankaccountservice.utils.AccountNumberGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.stb.bankaccountservice.utils.Constants.*;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final BankAccountTypeRepository bankAccountTypeRepository;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository,
                                  AccountNumberGenerator accountNumberGenerator,
                                  BankAccountTypeRepository bankAccountTypeRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.accountNumberGenerator = accountNumberGenerator;
        this.bankAccountTypeRepository = bankAccountTypeRepository;
    }

    @Override
    public BankAccount create(CreateBankAccountDTO createBankAccountDTO) {
        Optional<BankAccountType> bankAccountType = bankAccountTypeRepository.findByName(BASIC_ACCOUNT_TYPE);
        if (bankAccountType.isEmpty()) {
            BankAccountType basicAccountType = BankAccountType.builder()
                    .name(BASIC_ACCOUNT_TYPE)
                    .transactionLimit(BASIC_ACCOUNT_TYPE_TRANSACTION_LIMIT)
                    .build();
            bankAccountType = Optional.of(bankAccountTypeRepository.save(basicAccountType));
        }
        String accountNumber = accountNumberGenerator.generateAccountNumber(createBankAccountDTO.getName());

        BankAccount bankAccount = BankAccount.builder()
                .name(createBankAccountDTO.getName())
                .userId(createBankAccountDTO.getUserId())
                .number(accountNumber)
                .accountType(bankAccountType.get())
                .build();

        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount update(BankAccount bankAccount) {

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
