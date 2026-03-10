package com.mat.hexa_finance.application.ports.in.category;

import com.mat.hexa_finance.domain.model.Category;

import java.util.List;

public interface FindAllCategoriesUseCase {
    List<Category> execute();
}
