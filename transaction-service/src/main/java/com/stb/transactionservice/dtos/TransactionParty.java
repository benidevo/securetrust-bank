package com.stb.transactionservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionParty {
    @NotEmpty
    private String number;
    @NotEmpty
    private String name = "";
}
