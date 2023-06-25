package com.stb.bankaccountservice.utils.apiResponse;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApiResponse {
    private boolean success;
    private String message;
    private Object data = null;
    private Object error = null;

}