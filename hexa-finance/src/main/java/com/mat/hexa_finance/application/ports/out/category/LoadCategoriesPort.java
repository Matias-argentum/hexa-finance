package com.mat.hexa_finance.application.ports.out.category;

import com.mat.hexa_finance.domain.model.Category;

import java.util.List;

public interface LoadCategoriesPort {
    List<Category> findAll();
}
