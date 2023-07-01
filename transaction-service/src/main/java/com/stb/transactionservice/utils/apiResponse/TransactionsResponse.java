package com.stb.transactionservice.utils.apiResponse;

import com.stb.transactionservice.entities.Transaction;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TransactionsResponse<T extends List<Transaction>> {
    private boolean success;
    private String message;
    private T data;
    private Object error;
}
