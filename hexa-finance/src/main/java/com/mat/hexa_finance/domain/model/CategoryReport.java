package com.mat.hexa_finance.domain.model;

import java.math.BigDecimal;

public record CategoryReport(
        String categoryName,
        BigDecimal amount,
        BigDecimal percentage
) {
}
