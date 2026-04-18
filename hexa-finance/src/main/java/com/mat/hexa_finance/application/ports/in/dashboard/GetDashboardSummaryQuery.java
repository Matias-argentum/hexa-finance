package com.mat.hexa_finance.application.ports.in.dashboard;

import com.mat.hexa_finance.domain.model.DashboardSummary;

public interface GetDashboardSummaryQuery {
    DashboardSummary execute(Long userId, int month, int year);
}
