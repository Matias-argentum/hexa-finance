package com.mat.hexa_finance.infrastructure.adapters.out.persistence.dashboard;

import com.mat.hexa_finance.domain.model.DashboardSummary;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.transaction.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface JpaDashboardRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT SUM(t.amount) FROM TransactionEntity t " +
            "WHERE t.type = 'INCOME' AND t.user.id = :userId " +
            "AND t.date BETWEEN :from AND :to")
    BigDecimal getTotalMonthlyIncome(@Param("userId") Long userId,
                                     @Param("from") LocalDate from,
                                     @Param("to") LocalDate to);

    @Query("SELECT SUM(t.amount) FROM TransactionEntity t " +
            "WHERE t.type = 'EXPENSE' AND t.user.id = :userId " +
            "AND t.date BETWEEN :from AND :to")
    BigDecimal getTotalMonthlyExpense(@Param("userId") Long userId,
                                      @Param("from") LocalDate from,
                                      @Param("to") LocalDate to);
}
