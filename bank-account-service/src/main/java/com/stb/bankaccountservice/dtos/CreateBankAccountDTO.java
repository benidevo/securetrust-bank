package com.stb.bankaccountservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateBankAccountDTO {
    @NotNull
    private Long userId;

    @NotNull
    private String name;
}
