package com.stb.bankaccountservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentPayloadDTO {
    @NotEmpty
    private String type;

    @NotEmpty
    @Size(min = 10)
    private String docUrl;
}
