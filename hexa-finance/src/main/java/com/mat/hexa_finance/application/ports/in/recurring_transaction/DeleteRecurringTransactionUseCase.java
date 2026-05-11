package com.mat.hexa_finance.application.ports.in.recurring_transaction;

public interface DeleteRecurringTransactionUseCase {
    void delete(Long id, Long userId);
}
