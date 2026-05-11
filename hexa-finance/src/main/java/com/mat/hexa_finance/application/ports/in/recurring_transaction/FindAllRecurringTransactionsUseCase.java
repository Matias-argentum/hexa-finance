package com.mat.hexa_finance.application.ports.in.recurring_transaction;

import com.mat.hexa_finance.domain.model.RecurringTransactionDetail;

import java.util.List;

public interface FindAllRecurringTransactionsUseCase {
    List<RecurringTransactionDetail> execute(Long userId);
}
