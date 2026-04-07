package com.mat.hexa_finance.infrastructure.config;

import com.mat.hexa_finance.application.ports.out.transaction.CheckTransactionPort;
import com.mat.hexa_finance.application.ports.out.transaction.DeleteTransactionPort;
import com.mat.hexa_finance.application.ports.out.transaction.LoadTransactionsPort;
import com.mat.hexa_finance.application.ports.out.transaction.SaveTransactionPort;
import com.mat.hexa_finance.application.service.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionBeanConfig {

    @Bean
    public TransactionService transactionService(SaveTransactionPort saveTransactionPort,
                                                 DeleteTransactionPort deleteTransactionPort,
                                                 LoadTransactionsPort loadTransactionsPort,
                                                 CheckTransactionPort checkTransactionPort) {
        return new TransactionService(saveTransactionPort, deleteTransactionPort, loadTransactionsPort, checkTransactionPort);
    }
}
