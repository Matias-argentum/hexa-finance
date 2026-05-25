package com.mat.hexa_finance.application.ports.out.recurring_transaction;

import com.mat.hexa_finance.domain.model.RecurringTransaction;
import com.mat.hexa_finance.domain.model.RecurringTransactionDetail;

import java.util.List;
import java.util.Optional;

public interface LoadRecurringTransactionsPort {

    List<RecurringTransactionDetail> findAllByUserId(Long userId);

    List<RecurringTransaction> findAllByDayOfMonth(int dayOfMonth);

    Optional<RecurringTransactionDetail> findByIdAndUserId(Long id, Long userId);

    Optional<RecurringTransaction> getByIdAndUserId(Long id, Long userId);
}
