package com.mat.hexa_finance.application.ports.in.transaction;

import com.mat.hexa_finance.domain.model.TransactionDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface FindAllTransactionsUseCase {
    List<TransactionDetail> execute(Long userId);
    Page<TransactionDetail> executeForPagination(Long userId, LocalDate from, LocalDate to, Pageable pageable);
}
