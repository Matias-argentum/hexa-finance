package com.mat.hexa_finance.application.ports.in.transaction;

import com.mat.hexa_finance.domain.model.Transaction;
import com.mat.hexa_finance.domain.model.TransactionDetail;

public interface GetTransactionUseCase {
    TransactionDetail executeFindByIdAndUserId(Long id, Long userId);
    Transaction executeFindById(Long id);
}
