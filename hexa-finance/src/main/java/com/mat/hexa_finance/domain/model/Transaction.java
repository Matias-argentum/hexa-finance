package com.mat.hexa_finance.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class Transaction {
    private Long id;
    private BigDecimal amount;
    private String description;
    private TransactionType type;
    private Long categoryId;
    private LocalDate date;
    private Long userId;
}
