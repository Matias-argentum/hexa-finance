package com.mat.hexa_finance.infrastructure.adapters.out.persistence.report;

import com.mat.hexa_finance.application.ports.in.report.query.ReportFilter;
import com.mat.hexa_finance.application.ports.out.report.LoadReportPort;
import com.mat.hexa_finance.domain.model.CategoryReport;
import com.mat.hexa_finance.domain.model.FinancialReport;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.transaction.JpaTransactionRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class ReportPersistenceAdapter implements LoadReportPort {

    private final JpaTransactionRepository repository;

    public ReportPersistenceAdapter(JpaTransactionRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<CategoryReport> loadFinancialReport(Long userId, ReportFilter filter) {
        return repository.findReport(
                        userId,
                        filter.from(),
                        filter.to(),
                        filter.type(),
                        filter.categoryIds()
                )
                .stream()
                .map(crp -> new CategoryReport(
                        crp.categoryName(),
                        crp.totalAmount(),
                        crp.totalAmount().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                ))
                .toList();
    }
}
