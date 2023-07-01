package com.stb.transactionservice.utils.apiResponse;

import com.stb.transactionservice.entities.Transaction;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponse<T extends Transaction> {
    private boolean success;
    private String message;
    private T data;
    private Object error;
}
