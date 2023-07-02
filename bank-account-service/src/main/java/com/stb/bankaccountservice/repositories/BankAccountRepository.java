package com.stb.bankaccountservice.repositories;

import com.stb.bankaccountservice.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long>  {
    Optional<Object> findByNumber(String accountNumber);
}
