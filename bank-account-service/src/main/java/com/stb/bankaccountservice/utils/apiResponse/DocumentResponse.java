package com.stb.bankaccountservice.utils.apiResponse;

import com.stb.bankaccountservice.entities.Document;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocumentResponse <T extends Document> {
    private boolean success;
    private String message;
    private T data;
    private Object error;
}
