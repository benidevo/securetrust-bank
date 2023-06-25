package com.stb.bankaccountservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateBankAccountDTO {
    @Positive
    private Long userId;

    @NotEmpty
    @Size(min = 6, max = 50)
    private String name;
}
