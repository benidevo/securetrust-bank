package com.stb.bankaccountservice.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ValidationException extends RuntimeException {
    private final List<ErrorDetail> errors;
}
