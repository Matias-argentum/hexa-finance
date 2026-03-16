package com.mat.hexa_finance.application.service;

import com.mat.hexa_finance.application.ports.in.category.*;
import com.mat.hexa_finance.application.ports.in.category.command.CreateCategoryCommand;
import com.mat.hexa_finance.application.ports.in.category.command.UpdateCategoryCommand;
import com.mat.hexa_finance.application.ports.out.category.CheckCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.DeleteCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.LoadCategoriesPort;
import com.mat.hexa_finance.application.ports.out.category.SaveCategoryPort;
import com.mat.hexa_finance.domain.exception.CategoryNotFoundException;
import com.mat.hexa_finance.domain.model.Category;

import java.util.List;

public class CategoryService implements SaveCategoryUseCase,
        FindAllCategoriesUseCase,
        DeleteCategoryUseCase,
        UpdateCategoryUseCase,
        GetCategoryUseCase {
    private final SaveCategoryPort saveCategoryPort;
    private final LoadCategoriesPort loadCategoriesPort;
    private final CheckCategoryPort checkCategoryPort;
    private final DeleteCategoryPort deleteCategoryPort;

    public CategoryService(SaveCategoryPort saveCategoryPort,
                           LoadCategoriesPort loadCategoriesPort,
                           CheckCategoryPort checkCategoryPort,
                           DeleteCategoryPort deleteCategoryPort) {
        this.saveCategoryPort = saveCategoryPort;
        this.loadCategoriesPort = loadCategoriesPort;
        this.checkCategoryPort = checkCategoryPort;
        this.deleteCategoryPort = deleteCategoryPort;
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

    @Override
    public void delete(Long id) {
        if (!checkCategoryPort.existsById(id)) {
            throw new CategoryNotFoundException("Category does not exists");
        }
        deleteCategoryPort.deleteById(id);
    }

    @Override
    public Category update(UpdateCategoryCommand command) {
        Category foundCategory = loadCategoriesPort.findById(command.getId())
                .orElseThrow(() -> new CategoryNotFoundException("Category does not exists"));
        foundCategory.setName(command.getName());
        foundCategory.setIconName(command.getIconName());
        foundCategory.setUserId(command.getUserId());

        return saveCategoryPort.save(foundCategory);
    }

    @Override
    public Category execute(Long id) {
        return loadCategoriesPort.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category does not exists"));
    }
}
