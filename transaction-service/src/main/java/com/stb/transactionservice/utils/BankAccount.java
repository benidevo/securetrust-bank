package com.stb.transactionservice.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
//import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    private Long id;

    private String name;

    private String number;

    private Boolean isActive;

    private BigDecimal balance;

    private BankAccountType accountType;

    @JsonIgnore
    private Object documents;

    private Date createdAt;

    private Date updatedAt;
}
