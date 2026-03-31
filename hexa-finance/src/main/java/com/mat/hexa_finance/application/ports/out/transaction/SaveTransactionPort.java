package com.mat.hexa_finance.application.ports.out.transaction;

import com.mat.hexa_finance.domain.model.Transaction;

public interface SaveTransactionPort {
    Transaction save(Transaction transaction);
}
