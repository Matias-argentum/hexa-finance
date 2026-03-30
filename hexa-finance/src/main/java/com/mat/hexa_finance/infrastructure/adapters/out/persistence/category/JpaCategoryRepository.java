package com.mat.hexa_finance.infrastructure.adapters.out.persistence.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query("SELECT c FROM CategoryEntity c WHERE c.owner IS NULL OR c.owner.id = :userId")
    List<CategoryEntity> findAllGlobalAndByUserId(@Param("userId") Long userId);
}
