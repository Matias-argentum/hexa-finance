package com.mat.hexa_finance.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FinancialReport(
        LocalDate from,
        LocalDate to,
        List<CategoryReport> categories,
        TransactionType type,
        BigDecimal grandTotal
) {
}
