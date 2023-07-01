package com.stb.transactionservice.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ValidationException extends RuntimeException {
    private final List<ErrorDetail> errors;
}
