package com.mat.hexa_finance.application.service;

import com.mat.hexa_finance.application.ports.in.category.FindAllCategoriesUseCase;
import com.mat.hexa_finance.application.ports.in.category.SaveCategoryUseCase;
import com.mat.hexa_finance.application.ports.in.category.command.CreateCategoryCommand;
import com.mat.hexa_finance.application.ports.out.category.LoadCategoriesPort;
import com.mat.hexa_finance.application.ports.out.category.SaveCategoryPort;
import com.mat.hexa_finance.domain.model.Category;

import java.util.List;

public class CategoryService implements SaveCategoryUseCase, FindAllCategoriesUseCase {
    private final SaveCategoryPort saveCategoryPort;
    private final LoadCategoriesPort loadCategoriesPort;

    public CategoryService(SaveCategoryPort saveCategoryPort,
                           LoadCategoriesPort loadCategoriesPort){
        this.saveCategoryPort = saveCategoryPort;
        this.loadCategoriesPort = loadCategoriesPort;
    }

    @Override
    public Category execute(CreateCategoryCommand command) {
        Category category = new Category(null, command.getName(), command.getIconName(), command.getUserId());
        return saveCategoryPort.save(category);
    }

    @Override
    public List<Category> execute() {
        return loadCategoriesPort.findAll();
    }
}
