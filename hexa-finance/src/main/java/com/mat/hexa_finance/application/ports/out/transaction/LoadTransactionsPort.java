package com.mat.hexa_finance.application.ports.out.transaction;

import com.mat.hexa_finance.domain.model.Transaction;
import com.mat.hexa_finance.domain.model.TransactionDetail;

import java.util.List;
import java.util.Optional;

public interface LoadTransactionsPort {
    List<TransactionDetail> findAllByUserId(Long userId);
    Optional<Transaction> findById(Long id);
    Optional<TransactionDetail> findByIdAndUserId(Long id, Long userId);
}
