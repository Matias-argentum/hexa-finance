package com.mat.hexa_finance.application.ports.in.report;

import com.mat.hexa_finance.application.ports.in.report.query.ReportFilter;
import com.mat.hexa_finance.domain.model.FinancialReport;

public interface GetReportUseCase {
    FinancialReport execute(Long userId, ReportFilter reportFilter);
}
