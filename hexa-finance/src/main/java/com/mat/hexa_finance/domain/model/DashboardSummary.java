package com.mat.hexa_finance.domain.model;

import java.math.BigDecimal;

public record DashboardSummary(
        BigDecimal totalExpense,
        BigDecimal totalIncome
) {
    public BigDecimal calculateBalance(){
        return totalIncome.subtract(totalExpense);
    }
}
