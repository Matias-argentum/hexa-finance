package com.mat.hexa_finance.infrastructure.config;

import com.mat.hexa_finance.application.ports.out.recurring_transaction.LoadRecurringTransactionsPort;
import com.mat.hexa_finance.application.ports.out.recurring_transaction.SaveRecurringTransactionPort;
import com.mat.hexa_finance.application.ports.out.transaction.SaveTransactionPort;
import com.mat.hexa_finance.application.service.recurring_transaction.AutomatedRecurringTransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutomatedRecurringTransactionServiceBeanConfig {

    @Bean
    AutomatedRecurringTransactionService automatedRecurringTransactionService(
            LoadRecurringTransactionsPort loadRecurringTransactionsPort,
            SaveRecurringTransactionPort saveRecurringTransactionPort,
            SaveTransactionPort saveTransactionPort
    ) {
        return new AutomatedRecurringTransactionService(loadRecurringTransactionsPort, saveRecurringTransactionPort, saveTransactionPort);
    }
}
