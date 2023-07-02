package com.stb.transactionservice.dtos;

import com.stb.transactionservice.utils.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;

@Data
@Builder
public class CreateTransactionDTO {
    // TODO: Add validation
    private boolean isInternal;

    @NotNull(message = "type must be either CREDIT or DEBIT")
    private TransactionType type;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private String description = "";

    @NotNull
    private TransactionParty transactionParty;
}
