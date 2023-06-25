package com.stb.bankaccountservice.utils.apiResponse;


import com.stb.bankaccountservice.entities.BankAccountType;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class BankAccountTypeResponse<T extends BankAccountType>{
    private boolean success;
    private String message;
    private T data;
    private Object error;

}
