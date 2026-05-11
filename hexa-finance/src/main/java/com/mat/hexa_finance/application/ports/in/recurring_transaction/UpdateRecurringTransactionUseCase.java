package com.mat.hexa_finance.application.ports.in.recurring_transaction;

import com.mat.hexa_finance.application.ports.in.recurring_transaction.command.UpdateRecurringTransactionCommand;
import com.mat.hexa_finance.domain.model.RecurringTransaction;

public interface UpdateRecurringTransactionUseCase {
    RecurringTransaction update(UpdateRecurringTransactionCommand command);
}
