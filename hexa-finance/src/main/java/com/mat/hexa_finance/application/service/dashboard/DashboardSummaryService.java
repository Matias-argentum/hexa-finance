package com.mat.hexa_finance.application.service.dashboard;

import com.mat.hexa_finance.application.ports.in.dashboard.GetDashboardSummaryQuery;
import com.mat.hexa_finance.application.ports.out.dashboard.LoadBalancePort;
import com.mat.hexa_finance.domain.model.DashboardSummary;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class DashboardSummaryService implements GetDashboardSummaryQuery {

    private final LoadBalancePort loadBalancePort;

    public DashboardSummaryService(LoadBalancePort loadBalancePort) {
        this.loadBalancePort = loadBalancePort;
    }

    @Override
    public DashboardSummary execute(Long userId, int month, int year) {

        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = from.with(TemporalAdjusters.lastDayOfMonth());

        return loadBalancePort.loadBalance(userId, from, to);
    }
}
