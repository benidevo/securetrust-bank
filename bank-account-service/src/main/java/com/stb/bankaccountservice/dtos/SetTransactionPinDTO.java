package com.stb.bankaccountservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;


@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SetTransactionPinDTO {
    @NotEmpty
    @Size(min = 4, max = 4)
    private String pin;
}
