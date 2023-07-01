package com.stb.transactionservice.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Beneficiary {
    private String accountNumber;
    private String name;
    private String bank = "";
}
