package com.stb.bankaccountservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionPinDTO {
    @NotEmpty
    @Size(min = 4, max = 4)
    private String currentPin;

    @NotEmpty
    @Size(min = 4, max = 4)
    private String newPin;
}
