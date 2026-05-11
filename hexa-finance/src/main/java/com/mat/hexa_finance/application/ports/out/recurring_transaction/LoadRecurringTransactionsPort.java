package com.mat.hexa_finance.application.ports.out.recurring_transaction;

import com.mat.hexa_finance.domain.model.RecurringTransactionDetail;

import java.util.List;
import java.util.Optional;

public interface LoadRecurringTransactionsPort {

    List<RecurringTransactionDetail> findAllByUserId(Long userId);

    Optional<RecurringTransactionDetail> findByIdAndUserId(Long id, Long userId);
}
