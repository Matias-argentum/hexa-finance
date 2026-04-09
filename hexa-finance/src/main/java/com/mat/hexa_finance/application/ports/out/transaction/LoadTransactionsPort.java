package com.mat.hexa_finance.application.ports.out.transaction;

import com.mat.hexa_finance.domain.model.Transaction;
import com.mat.hexa_finance.domain.model.TransactionDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoadTransactionsPort {
    List<TransactionDetail> findAllByUserId(Long userId);
    Optional<Transaction> findById(Long id);
    Optional<TransactionDetail> findByIdAndUserId(Long id, Long userId);

    Page<TransactionDetail> findAllByUserIdPaginated(Long userId, LocalDate from, LocalDate to, Pageable pageable);
}
