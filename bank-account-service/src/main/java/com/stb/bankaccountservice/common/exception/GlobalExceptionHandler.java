package com.stb.bankaccountservice.common.exception;

import com.stb.bankaccountservice.utils.apiResponse.ApiResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Component
@ControllerAdvice
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
        return  new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getParameter().getParameterName();
        String errorMessage = "Invalid value provided for parameter '" + parameterName + "'";

        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .message(errorMessage)
                .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ApiResponse> handleEntityExistsException(EntityExistsException ex) {
        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build(),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .message("Required request body is missing")
                .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
