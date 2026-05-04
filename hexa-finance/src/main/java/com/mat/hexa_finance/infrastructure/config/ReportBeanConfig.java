package com.mat.hexa_finance.infrastructure.config;

import com.mat.hexa_finance.application.ports.in.report.GetReportUseCase;
import com.mat.hexa_finance.application.ports.out.report.LoadReportPort;
import com.mat.hexa_finance.application.service.report.ReportService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportBeanConfig {

    @Bean
    public GetReportUseCase getReportUseCase(LoadReportPort loadReportPort) {
        return new ReportService(loadReportPort);
    }
}