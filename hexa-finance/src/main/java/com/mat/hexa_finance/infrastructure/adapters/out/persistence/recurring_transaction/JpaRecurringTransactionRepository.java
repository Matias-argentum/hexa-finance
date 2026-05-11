package com.mat.hexa_finance.infrastructure.adapters.out.persistence.recurring_transaction;

import com.mat.hexa_finance.infrastructure.adapters.out.persistence.transaction.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaRecurringTransactionRepository extends JpaRepository<RecurringTransactionEntity, Long> {
    @Query("SELECT rt FROM RecurringTransactionEntity rt JOIN FETCH rt.category WHERE rt.user.id = :userId ORDER BY rt.id DESC")
    List<RecurringTransactionEntity> findAllByUserIdWithCategory(@Param("userId") Long userId);

    @Query("SELECT rt FROM RecurringTransactionEntity rt JOIN FETCH rt.category WHERE rt.id = :id AND rt.user.id = :userId")
    Optional<RecurringTransactionEntity> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}
