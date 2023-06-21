package com.stb.bankaccountservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "bank_account_type")
public class BankAccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "transaction_limit", nullable = false, columnDefinition = "DECIMAL(10,2) default '0.0'")
    private BigDecimal transactionLimit;

    @Column(name = "unlimited", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean unlimited;

    @OneToMany(mappedBy = "accountType")
    private List<BankAccount> bankAccounts;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
