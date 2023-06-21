package com.stb.bankaccountservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocumentDTO {
    @NotNull
    private String type;

    @NotNull
    private String docUrl;
}
