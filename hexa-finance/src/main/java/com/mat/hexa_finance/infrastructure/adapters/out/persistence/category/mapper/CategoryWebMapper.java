package com.mat.hexa_finance.infrastructure.adapters.out.persistence.category.mapper;

import com.mat.hexa_finance.domain.model.Category;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.category.dto.CategoryResponse;

public class CategoryWebMapper {
    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getIconName()
        );
    }
}
