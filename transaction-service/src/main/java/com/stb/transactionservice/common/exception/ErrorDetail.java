package com.stb.transactionservice.common.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDetail {
    private String field;
    private String message;
}
