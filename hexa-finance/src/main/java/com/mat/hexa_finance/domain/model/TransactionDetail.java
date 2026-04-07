package com.mat.hexa_finance.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class TransactionDetail {
    private Long id;
    private BigDecimal amount;
    private String description;
    private TransactionType type;
    private LocalDate date;
    private String categoryName;
    private String categoryIcon;
}
