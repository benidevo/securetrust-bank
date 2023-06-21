package com.stb.bankaccountservice.repositories;

import com.stb.bankaccountservice.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
