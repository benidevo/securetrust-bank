package com.stb.bankaccountservice.repositories;

import com.stb.bankaccountservice.entities.BankAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountTypeRepository extends JpaRepository<BankAccountType, Long> {
}
