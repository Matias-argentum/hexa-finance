package com.mat.hexa_finance.application.ports.in.recurring_transaction;


import com.mat.hexa_finance.application.ports.in.recurring_transaction.command.CreateRecurringTransactionCommand;
import com.mat.hexa_finance.domain.model.RecurringTransaction;

public interface SaveRecurringTransactionUserCase {
    RecurringTransaction execute(CreateRecurringTransactionCommand command);
}
