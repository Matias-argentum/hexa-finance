package com.mat.hexa_finance.application.service;

import com.mat.hexa_finance.application.ports.in.category.SaveCategoryUseCase;
import com.mat.hexa_finance.application.ports.in.category.command.CreateCategoryCommand;
import com.mat.hexa_finance.application.ports.out.category.SaveCategoryPort;
import com.mat.hexa_finance.domain.model.Category;

public class CategoryService implements SaveCategoryUseCase {
    private final SaveCategoryPort saveCategoryPort;

    public CategoryService(SaveCategoryPort saveCategoryPort){
        this.saveCategoryPort = saveCategoryPort;
    }

    @Override
    public Category execute(CreateCategoryCommand command) {
        Category category = new Category(null, command.getName(), command.getIconName(), command.getUserId());
        return saveCategoryPort.save(category);
    }
}
