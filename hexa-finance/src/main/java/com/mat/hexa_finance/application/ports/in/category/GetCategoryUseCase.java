package com.mat.hexa_finance.application.ports.in.category;

import com.mat.hexa_finance.domain.model.Category;

public interface GetCategoryUseCase {
    Category execute(Long id);
}
