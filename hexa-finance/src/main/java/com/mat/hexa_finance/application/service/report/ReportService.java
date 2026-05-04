package com.mat.hexa_finance.application.service.report;

import com.mat.hexa_finance.application.ports.in.report.GetReportUseCase;
import com.mat.hexa_finance.application.ports.in.report.query.ReportFilter;
import com.mat.hexa_finance.application.ports.out.report.LoadReportPort;
import com.mat.hexa_finance.domain.model.CategoryReport;
import com.mat.hexa_finance.domain.model.FinancialReport;

import java.math.BigDecimal;
import java.util.List;

public class ReportService implements GetReportUseCase {

    private final LoadReportPort loadReportPort;

    public ReportService(LoadReportPort loadReportPort) {
        this.loadReportPort = loadReportPort;
    }

    @Override
    public FinancialReport execute(Long userId, ReportFilter reportFilter) {
        List<CategoryReport> categoryReportList = loadReportPort.loadFinancialReport(userId, reportFilter);
        BigDecimal grandTotal = categoryReportList.stream().map(CategoryReport::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new FinancialReport(reportFilter.from(), reportFilter.to(), categoryReportList, grandTotal);
    }
}
