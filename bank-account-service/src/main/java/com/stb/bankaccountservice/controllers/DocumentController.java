package com.stb.bankaccountservice.controllers;

import com.stb.bankaccountservice.common.annotation.HandleValidationErrors;
import com.stb.bankaccountservice.dtos.DocumentPayloadDTO;
import com.stb.bankaccountservice.entities.Document;
import com.stb.bankaccountservice.services.DocumentService;
import com.stb.bankaccountservice.utils.apiResponse.ApiResponse;
import com.stb.bankaccountservice.utils.apiResponse.DocumentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Document")
@RestController
@RequestMapping("/api/v1/accounts/{id}/documents")
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Operation(description = "Add document to bank account")
    @PostMapping
    @HandleValidationErrors
    public ResponseEntity<DocumentResponse<Document>> create(@PathVariable Long id, @Valid
                                                             @RequestBody DocumentPayloadDTO documentPayloadDTO,
                                                             BindingResult result) {
        Document newDocument = documentService.create(id, documentPayloadDTO);
        DocumentResponse<Document> response = DocumentResponse.builder()
                .success(true)
                .message("Document created successfully")
                .data(newDocument)
                .build();

        return  new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(description = "List all documents")
    @GetMapping
    public ResponseEntity<ApiResponse> list(@PathVariable Long id) {
        List<Document> documents = documentService.list(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Documents retrieved successfully")
                .data(documents)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Update document")
    @PutMapping("/{documentId}")
    @HandleValidationErrors
    public ResponseEntity<DocumentResponse<Document>> update(@PathVariable Long documentId,
                                                             @PathVariable Long id,
                                                             @Valid @RequestBody DocumentPayloadDTO documentPayloadDTO,
                                                             BindingResult result) {
        Document document = documentService.update(id, documentId, documentPayloadDTO);
        DocumentResponse<Document> response = DocumentResponse.builder()
                .success(true)
                .message("Document updated successfully")
                .data(document)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Get document by id")
    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentResponse<Document>> get(@PathVariable Long documentId, @PathVariable Long id) {
        Document document = documentService.get(id, documentId);
        DocumentResponse<Document> response = DocumentResponse.builder()
                .success(true)
                .message("Document retrieved successfully")
                .data(document)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Delete Document")
    @DeleteMapping("/{documentId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long documentId, @PathVariable Long id) {
        documentService.delete(id, documentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Document deleted successfully")
                .build();

        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
