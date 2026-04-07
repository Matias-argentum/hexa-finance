package com.mat.hexa_finance.application.ports.in.transaction;

import com.mat.hexa_finance.domain.model.TransactionDetail;

import java.util.List;

public interface FindAllTransactionsUseCase {
    List<TransactionDetail> execute(Long userId);
}
