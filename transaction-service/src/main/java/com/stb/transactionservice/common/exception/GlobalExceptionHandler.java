package com.stb.transactionservice.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stb.transactionservice.utils.apiResponse.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@Component
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .message("Validation error")
                .error(ex.getErrors())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getParameter().getParameterName();
        String errorMessage = "Invalid value provided for parameter '" + parameterName + "'";

        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .message(errorMessage)
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        log.error("HttpRequestMethodNotSupportedException: ", ex);
        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build(),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException: ", ex);
        return  new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .message("Required request body is missing")
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public void handleJsonProcessingException(JsonProcessingException ex) {
        log.error("JsonProcessingException: ", ex);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse> handleResponseStatusException(ResponseStatusException ex) {
        log.error("ResponseStatusException: ", ex);
        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .message(ex.getReason())
                .build(),
                ex.getStatusCode()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build(),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception ex) {
        log.error("Internal Server Error: ", ex);
        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .message("Internal server error")
                .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
