package com.mat.hexa_finance.application.ports.out.recurring_transaction;

import com.mat.hexa_finance.domain.model.RecurringTransaction;
import com.mat.hexa_finance.domain.model.RecurringTransactionDetail;

public interface SaveRecurringTransactionPort {
    RecurringTransaction save(RecurringTransaction transaction);
}
