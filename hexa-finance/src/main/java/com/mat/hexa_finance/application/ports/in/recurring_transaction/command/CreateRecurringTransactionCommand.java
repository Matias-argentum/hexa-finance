package com.mat.hexa_finance.application.ports.in.recurring_transaction.command;

import com.mat.hexa_finance.domain.model.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class CreateRecurringTransactionCommand {
    private BigDecimal amount;
    private String description;
    private TransactionType type;
    private Integer dayOfMonth;
    private Long categoryId;
    private Long userId;
}
