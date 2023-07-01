package com.stb.transactionservice.dtos;

import com.stb.transactionservice.utils.TransactionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;

@Data
@Builder
public class CreateTransactionDTO {
    @NotEmpty
    private boolean isInternal;

    @NotEmpty
    private TransactionType type;

    @NotEmpty
    @Positive
    private BigDecimal amount;

    @NotNull
    private String description = "";

    @NotEmpty
    @Positive
    private Long bankAccountId;

    @NotEmpty
    private Beneficiary beneficiary;

}
