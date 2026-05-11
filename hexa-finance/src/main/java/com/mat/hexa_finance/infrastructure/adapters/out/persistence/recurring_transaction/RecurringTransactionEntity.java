package com.mat.hexa_finance.infrastructure.adapters.out.persistence.recurring_transaction;

import com.mat.hexa_finance.domain.model.TransactionType;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.category.CategoryEntity;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "recurring_transactions")
@Getter
@Setter
public class RecurringTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @NotNull
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @NotNull
    private Integer dayOfMonth;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private CategoryEntity category;

    @NotNull
    private boolean isActive;

    private LocalDate lastExecutedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private UserEntity user;
}
