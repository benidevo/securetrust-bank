package com.stb.bankaccountservice.repositories;

import com.stb.bankaccountservice.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByDocUrl(String docUrl);
}
