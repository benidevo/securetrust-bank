package com.stb.transactionservice.utils.httpClient.responses;


import com.stb.transactionservice.utils.BankAccount;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BankAccountResponse {
    private boolean success;
    private String message;
    private BankAccount data;
    private Object error;

    public boolean getSuccess() {
        return success;
    }
}
