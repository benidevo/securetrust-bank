package com.stb.bankaccountservice.services;

import com.stb.bankaccountservice.dtos.CreateDocumentDTO;
import com.stb.bankaccountservice.entities.Document;

import java.util.List;

public interface DocumentService {
    Document create(CreateDocumentDTO createDocumentDTO);

    Document update(Document document);

    Document get(Long id);

    Document getByDocUrl(String docUrl);

    List<Document> list();

    void delete(Long id);
}
