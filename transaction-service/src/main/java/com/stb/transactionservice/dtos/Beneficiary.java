package com.stb.transactionservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Beneficiary {
    @NotEmpty
    private String accountNumber;
    @NotEmpty
    private String name;
    private String bank = "";
}
