package com.in28minutes.springboot.myfirstwebapp.entity;

import com.in28minutes.springboot.myfirstwebapp.entity.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Many accounts belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private User user;


    @Column(name = "account_no", nullable = false, unique = true, length = 30)
    private String accountNo;


    @Column(name = "account_name", nullable = false, length = 100)
    private String accountName;


    @Column(name = "account_type", nullable = false, length = 50)
    private String accountType;
    // SAVINGS, CURRENT, LOAN, FD


    @Column(name = "product_code", length = 10)
    private String productCode;


    @Column(name = "currency", nullable = false, length = 3)
    private String currency;
    // USD, KHR


    @Column(
            name = "available_balance",
            precision = 18,
            scale = 2
    )
    private BigDecimal availableBalance;


    @Column(
            name = "ledger_balance",
            precision = 18,
            scale = 2
    )
    private BigDecimal ledgerBalance;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private AccountStatus status;


    @Column(name = "open_date")
    private LocalDate openDate;


    @Column(name = "close_date")
    private LocalDate closeDate;


    @Column(name = "created_by", length = 50)
    private String createdBy;


    @Column(name = "updated_by", length = 50)
    private String updatedBy;
}