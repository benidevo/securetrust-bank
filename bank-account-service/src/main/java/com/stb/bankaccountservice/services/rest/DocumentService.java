package com.stb.bankaccountservice.services.rest;

import com.stb.bankaccountservice.dtos.DocumentPayloadDTO;
import com.stb.bankaccountservice.entities.Document;

import java.util.List;

public interface DocumentService {
    Document create(Long bankAccountId, DocumentPayloadDTO documentPayloadDTO);

    Document update(Long bankAccountId, Long documentId, DocumentPayloadDTO documentPayloadDTO);

    Document get(Long bankAccountId, Long documentId);

    Document getByDocUrl(String docUrl);

    List<Document> list(Long bankAccountId);

    void delete(Long bankAccountId, Long documentId);
}
