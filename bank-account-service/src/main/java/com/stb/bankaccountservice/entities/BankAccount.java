package com.stb.bankaccountservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "bank_account_type_id")
    private BankAccountType accountType;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private List<Document> documents;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;
}
