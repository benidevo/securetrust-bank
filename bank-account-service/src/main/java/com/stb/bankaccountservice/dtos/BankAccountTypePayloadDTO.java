package com.stb.bankaccountservice.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountTypePayloadDTO {
    @Size(min = 4, max = 50)
    @NotEmpty
    private String name;

    @NotNull
    @DecimalMin(value = "10000.00")
    private BigDecimal transactionLimit;

    private boolean unlimited = false;
}
