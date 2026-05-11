package com.mat.hexa_finance.infrastructure.config;

import com.mat.hexa_finance.application.ports.out.recurring_transaction.DeleteRecurringTransactionPort;
import com.mat.hexa_finance.application.ports.out.recurring_transaction.LoadRecurringTransactionsPort;
import com.mat.hexa_finance.application.ports.out.recurring_transaction.SaveRecurringTransactionPort;
import com.mat.hexa_finance.application.service.recurring_transaction.RecurringTransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecurringTransactionBeanConfig {
    @Bean
    RecurringTransactionService recurringTransactionService(SaveRecurringTransactionPort saveRecurringTransactionPort,
                                                            LoadRecurringTransactionsPort loadRecurringTransactionsPort,
                                                            DeleteRecurringTransactionPort deleteRecurringTransactionPort){
        return new RecurringTransactionService(deleteRecurringTransactionPort, saveRecurringTransactionPort, loadRecurringTransactionsPort);
    }
}
