package com.mat.hexa_finance.application.ports.out.transaction;

import com.mat.hexa_finance.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface LoadTransactionsPort {
    List<Transaction> findAllByUserId(Long userId);
    Optional<Transaction> findById8(Long id);
}
