package com.mat.hexa_finance.application.ports.in.report.query;

import com.mat.hexa_finance.domain.model.TransactionType;

import java.time.LocalDate;
import java.util.List;

public record ReportFilter(
        LocalDate from,
        LocalDate to,
        TransactionType type,
        List<Long> categoryIds
) {}
