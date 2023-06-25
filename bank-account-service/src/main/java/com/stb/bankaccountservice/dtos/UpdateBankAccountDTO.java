package com.stb.bankaccountservice.dtos;

import jakarta.validation.constraints.*;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateBankAccountDTO {
    @Size(min = 6, max = 50)
    @NotEmpty
    @NotNull
    private String name;

    // TODO: add validation
    private boolean isActive;
}
