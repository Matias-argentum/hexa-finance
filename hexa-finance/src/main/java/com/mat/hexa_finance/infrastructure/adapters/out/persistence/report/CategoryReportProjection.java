package com.mat.hexa_finance.infrastructure.adapters.out.persistence.report;

import java.math.BigDecimal;

public record CategoryReportProjection(
        String categoryName,
        BigDecimal totalAmount
) {
}
