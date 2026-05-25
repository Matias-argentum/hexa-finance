package com.mat.hexa_finance.application.ports.in.recurring_transaction;

import com.mat.hexa_finance.domain.model.RecurringTransactionDetail;

public interface ToggleRecurringTransactionStatus {
    RecurringTransactionDetail execute(Long id, Long userId);
}
