package com.mat.hexa_finance.application.ports.out.category;

import com.mat.hexa_finance.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface LoadCategoriesPort {
    List<Category> findAll();
    Optional<Category> findById(Long id);
}
