package com.mat.hexa_finance.infrastructure.config;

import com.mat.hexa_finance.application.ports.out.dashboard.LoadBalancePort;
import com.mat.hexa_finance.application.service.dashboard.DashboardSummaryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DashboardBeanConfig {

    @Bean
    public DashboardSummaryService dashboardSummaryService(LoadBalancePort loadBalancePort){
        return new DashboardSummaryService(loadBalancePort);
    }
}
