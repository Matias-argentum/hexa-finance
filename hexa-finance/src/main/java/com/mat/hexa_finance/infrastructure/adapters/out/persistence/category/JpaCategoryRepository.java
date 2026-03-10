package com.mat.hexa_finance.infrastructure.adapters.out.persistence.category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
