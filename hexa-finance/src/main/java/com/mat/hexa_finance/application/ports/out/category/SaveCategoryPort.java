package com.mat.hexa_finance.application.ports.out.category;

import com.mat.hexa_finance.domain.model.Category;

public interface SaveCategoryPort {
    Category save(Category category);
}
