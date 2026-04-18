package com.mat.hexa_finance.application.ports.out.dashboard;

import com.mat.hexa_finance.domain.model.DashboardSummary;

import java.time.LocalDate;

public interface LoadBalancePort {
    DashboardSummary loadBalance(Long userId, LocalDate from, LocalDate to);
}