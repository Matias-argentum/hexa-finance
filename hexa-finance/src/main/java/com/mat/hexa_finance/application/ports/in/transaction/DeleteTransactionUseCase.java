package com.mat.hexa_finance.application.ports.in.transaction;

public interface DeleteTransactionUseCase {
    void delete(Long id, Long userId);
}
