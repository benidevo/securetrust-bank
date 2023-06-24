package com.stb.bankaccountservice.repositories;

import com.stb.bankaccountservice.entities.BankAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountTypeRepository extends JpaRepository<BankAccountType, Long> {
    Optional<BankAccountType> findByName(String name);
}
