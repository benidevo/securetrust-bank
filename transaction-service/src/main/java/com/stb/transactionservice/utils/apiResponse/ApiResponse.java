package com.stb.transactionservice.utils.apiResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    private boolean success;
    private String message;
    private Object data = null;
    private Object error = null;
}
