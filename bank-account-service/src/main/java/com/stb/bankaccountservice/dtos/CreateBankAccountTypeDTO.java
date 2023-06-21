package com.stb.bankaccountservice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;


@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateBankAccountTypeDTO {
    @NotNull
    private String name;

    @NotNull
    @Positive
    private BigDecimal transactionLimit;
}
