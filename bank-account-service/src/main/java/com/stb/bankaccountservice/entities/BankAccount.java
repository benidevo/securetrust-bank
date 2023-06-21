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
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive;

    @Column(name = "balance", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT '0.0'")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "bank_account_type_id")
    private BankAccountType accountType;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Document> documents;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
