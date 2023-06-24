package com.stb.bankaccountservice.services.rest.imp;

import com.stb.bankaccountservice.dtos.CreateDocumentDTO;
import com.stb.bankaccountservice.entities.Document;
import com.stb.bankaccountservice.repositories.DocumentRepository;
import com.stb.bankaccountservice.services.rest.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Document create(CreateDocumentDTO createDocumentDTO) {
        Document document = Document.builder()
                .type(createDocumentDTO.getType())
                .docUrl(createDocumentDTO.getType())
                .build();

        return documentRepository.save(document);
    }

    @Override
    public Document update(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public Document get(Long id) {
        return documentRepository.findById(id).
                orElseThrow( () -> new EntityNotFoundException("Document not found"));
    }

    @Override
    public Document getByDocUrl(String docUrl) {
       return (Document) documentRepository.findByDocUrl(docUrl).
                orElseThrow(() -> new EntityNotFoundException("Document not found"));
    }

    @Override
    public List<Document> list() {
        return documentRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        documentRepository.deleteById(id);
    }
}
