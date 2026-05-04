package com.mat.hexa_finance.application.ports.out.report;

import com.mat.hexa_finance.application.ports.in.report.query.ReportFilter;
import com.mat.hexa_finance.domain.model.CategoryReport;

import java.util.List;

public interface LoadReportPort {
    List<CategoryReport> loadFinancialReport(Long userId, ReportFilter filter);
}
