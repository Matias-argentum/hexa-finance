package com.mat.hexa_finance.infrastructure.adapters.in.web.transaction.dto;

import com.mat.hexa_finance.domain.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse (
         Long id,
         BigDecimal amount,
         String description,
         String type,
         LocalDate date,
         String categoryName,
         String categoryIcon
){

}
