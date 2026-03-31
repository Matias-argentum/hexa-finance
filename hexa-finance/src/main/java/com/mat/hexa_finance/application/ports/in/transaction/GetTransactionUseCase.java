package com.mat.hexa_finance.application.ports.in.transaction;

import com.mat.hexa_finance.domain.model.Transaction;

public interface GetTransactionUseCase {
    Transaction executeFindById(Long userId);
}
