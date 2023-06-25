package com.stb.bankaccountservice.utils.apiResponse;

import com.stb.bankaccountservice.entities.BankAccount;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankAccountResponse<T extends  BankAccount> {
    private boolean success;
    private String message;
    private T data;
    private Object error;
}
