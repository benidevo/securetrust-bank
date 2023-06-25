package com.stb.bankaccountservice.services.rest.impl;

import com.stb.bankaccountservice.dtos.DocumentPayloadDTO;
import com.stb.bankaccountservice.entities.BankAccount;
import com.stb.bankaccountservice.entities.Document;
import com.stb.bankaccountservice.repositories.BankAccountRepository;
import com.stb.bankaccountservice.repositories.DocumentRepository;
import com.stb.bankaccountservice.services.rest.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, BankAccountRepository bankAccountRepository) {
        this.documentRepository = documentRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Document create(Long bankAccountId, DocumentPayloadDTO documentPayloadDTO) {
        Optional<Document> existingDocument = documentRepository.findByDocUrl(documentPayloadDTO.getDocUrl());
        if (existingDocument.isPresent()) {
            throw new EntityNotFoundException("Document already exists");
        }

        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Bank account not found"));
        Document document = Document.builder()
                .type(documentPayloadDTO.getType())
                .docUrl(documentPayloadDTO.getDocUrl())
                .build();
        documentRepository.save(document);
        bankAccount.getDocuments().add(document);
        bankAccountRepository.save(bankAccount);


        return document;
    }

    @Override
    public Document update(Long bankAccountId, Long documentId, DocumentPayloadDTO documentPayloadDTO) {
        Document document = this.get(bankAccountId, documentId);
        document.setDocUrl(documentPayloadDTO.getDocUrl());
        document.setType(documentPayloadDTO.getType());

        return documentRepository.save(document);
    }

    @Override
    public Document get(Long bankAccountId, Long documentId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Bank account not found"));

        List<Document> documents = bankAccount.getDocuments();
        Document foundDocument = null;
        for (Document doc : documents) {
            if (doc.getId().equals(documentId)) {
                foundDocument = doc;
            }
        }

        if (foundDocument == null) {
            throw new EntityNotFoundException("Document not found");
        }

        return foundDocument;
    }

    @Override
    public Document getByDocUrl(String docUrl) {
       return (Document) documentRepository.findByDocUrl(docUrl).
                orElseThrow(() -> new EntityNotFoundException("Document not found"));
    }

    @Override
    public List<Document> list(Long bankAccountId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Bank account not found"));
        return  bankAccount.getDocuments();
    }

    @Override
    public void delete(Long bankAccountId, Long documentId) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
                        .orElseThrow(() -> new EntityNotFoundException("Bank account not found"));

        List<Document> documents = bankAccount.getDocuments();
        Document foundDocument = null;
        for (Document doc : documents) {
            if (doc.getId().equals(documentId)) {
                foundDocument = doc;
            }
        }

        if (foundDocument == null) {
            throw new EntityNotFoundException("Document not found");
        }

        documents.remove(foundDocument);
        bankAccountRepository.save(bankAccount);

        documentRepository.delete(foundDocument);
    }
}
