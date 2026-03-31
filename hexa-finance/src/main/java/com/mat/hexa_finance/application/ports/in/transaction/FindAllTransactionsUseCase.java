package com.mat.hexa_finance.application.ports.in.transaction;

import com.mat.hexa_finance.domain.model.Transaction;

import java.util.List;

public interface FindAllTransactionsUseCase {
    List<Transaction> execute(Long userId);
}
