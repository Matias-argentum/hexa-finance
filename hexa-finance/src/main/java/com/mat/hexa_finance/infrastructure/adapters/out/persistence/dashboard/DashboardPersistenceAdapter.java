package com.mat.hexa_finance.infrastructure.adapters.out.persistence.dashboard;

import com.mat.hexa_finance.application.ports.out.dashboard.LoadBalancePort;
import com.mat.hexa_finance.domain.model.DashboardSummary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DashboardPersistenceAdapter implements LoadBalancePort {

    private final JpaDashboardRepository dashboardRepository;

    public DashboardPersistenceAdapter(JpaDashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @Override
    public DashboardSummary loadBalance(Long userId, LocalDate from, LocalDate to) {

        BigDecimal totalIncome = dashboardRepository.getTotalMonthlyIncome(userId, from, to);
        BigDecimal totalExpense = dashboardRepository.getTotalMonthlyExpense(userId, from, to);

        return new DashboardSummary(totalExpense != null ? totalExpense : BigDecimal.ZERO, totalIncome != null ? totalIncome : BigDecimal.ZERO);
    }
}
