package com.mat.hexa_finance.application.ports.in.transaction.command;

import com.mat.hexa_finance.domain.model.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class CreateTransactionCommand {
    private BigDecimal amount;
    private String description;
    private TransactionType type;
    private Long categoryId;
    private LocalDate date;
    private Long userId;
}
