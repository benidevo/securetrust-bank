package com.stb.transactionservice.entities;

import com.stb.transactionservice.dtos.TransactionParty;
import com.stb.transactionservice.utils.TransactionType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Document(collection = "transaction")
public class Transaction {
    @Id
    private String id;

    @Field("type")
    private TransactionType type;

    @Field("amount")
    private BigDecimal amount;

    @Field("description")
    private String description;

    @Field("bank_account_id")
    private Long bankAccountId;

    @Field("transaction_party")
    private TransactionParty transactionParty;

    @CreatedDate
    @Field("created_at")
    private Date createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private Date updatedAt;
}
