package com.stb.transactionservice.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BankAccountType {
    private Long id;

    private String name;

    private BigDecimal transactionLimit;

    private Boolean unlimited;

    private Date createdAt;

    private Date updatedAt;
}
