package com.mat.hexa_finance.infrastructure.adapters.in.scheduling;

import com.mat.hexa_finance.application.ports.in.recurring_transaction.ProcessRecurringTransactionUserCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RecurringTransactionScheduler  {

    private final ProcessRecurringTransactionUserCase processRecurringTransactionUserCase;

    public RecurringTransactionScheduler(ProcessRecurringTransactionUserCase processRecurringTransactionUserCase) {
        this.processRecurringTransactionUserCase = processRecurringTransactionUserCase;
    }

    @Scheduled(cron = "${app.scheduling.recurring-transactions-cron}")
    public void run(){
        System.out.println("SE VA A EJECUTAR EL SCHEDULER");
        processRecurringTransactionUserCase.execute();
    }

}
