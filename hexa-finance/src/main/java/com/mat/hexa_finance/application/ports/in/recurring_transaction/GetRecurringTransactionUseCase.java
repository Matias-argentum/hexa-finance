package com.mat.hexa_finance.application.ports.in.recurring_transaction;

import com.mat.hexa_finance.domain.model.RecurringTransaction;
import com.mat.hexa_finance.domain.model.RecurringTransactionDetail;

public interface GetRecurringTransactionUseCase {
    RecurringTransactionDetail executeFindByIdAndUserId(Long id, Long userId);
    RecurringTransaction executeFindById(Long id);
}
