package com.stb.bankaccountservice.repositories;

import com.stb.bankaccountservice.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long>  {
}
