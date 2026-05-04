package com.mat.hexa_finance.infrastructure.adapters.out.persistence.transaction;

import com.mat.hexa_finance.domain.model.TransactionType;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.report.CategoryReportProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, Long> {

    //consulta que me trae las transacciones más la categoria para popular los TransactionDetail
    @Query("SELECT t FROM TransactionEntity t JOIN FETCH t.category WHERE t.user.id = :userId ORDER BY t.date DESC")
    List<TransactionEntity> findAllByUserIdWithCategory(@Param("userId") Long userId);

    // consulta para usar los filtros en la apginacion al traer las transacciones
    @Query("SELECT t FROM TransactionEntity t JOIN FETCH t.category " +
            "WHERE t.user.id = :userId AND t.date BETWEEN :from AND :to ORDER BY t.date DESC")
    Page<TransactionEntity> findAllByUserIdAndDateBetween(
            @Param("userId") Long userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable);

    @Query("SELECT t FROM TransactionEntity t JOIN FETCH t.category WHERE t.id = :id AND t.user.id = :userId")
    Optional<TransactionEntity> findByIdAndUserId(@Param("id") Long od, @Param("userId") Long userId);

    @Query("""
            SELECT new com.mat.hexa_finance.infrastructure.adapters.out.persistence.report.CategoryReportProjection(
                t.category.name, 
                SUM(t.amount)
            )
            FROM TransactionEntity t
            WHERE t.user.id = :userId
            AND
            (:categoryIds IS NULL OR t.category.id IN (:categoryIds))
            AND
            t.date BETWEEN :from AND :to
            AND t.type = :type
            GROUP BY t.category.name
            """)
    List<CategoryReportProjection> findReport(@Param("userId") Long userId,
                                              @Param("from") LocalDate from,
                                              @Param("to") LocalDate to,
                                              @Param("type") TransactionType type,
                                              @Param("categoryIds") List<Long> categoryIds);
}
