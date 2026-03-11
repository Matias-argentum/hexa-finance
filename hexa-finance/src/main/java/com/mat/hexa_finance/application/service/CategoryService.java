package com.mat.hexa_finance.application.service;

import com.mat.hexa_finance.application.ports.in.category.DeleteCategoryUseCase;
import com.mat.hexa_finance.application.ports.in.category.FindAllCategoriesUseCase;
import com.mat.hexa_finance.application.ports.in.category.SaveCategoryUseCase;
import com.mat.hexa_finance.application.ports.in.category.command.CreateCategoryCommand;
import com.mat.hexa_finance.application.ports.out.category.CheckCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.DeleteCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.LoadCategoriesPort;
import com.mat.hexa_finance.application.ports.out.category.SaveCategoryPort;
import com.mat.hexa_finance.domain.exception.CategoryNotFoundException;
import com.mat.hexa_finance.domain.model.Category;

import java.util.List;

public class CategoryService implements SaveCategoryUseCase, FindAllCategoriesUseCase, DeleteCategoryUseCase {
    private final SaveCategoryPort saveCategoryPort;
    private final LoadCategoriesPort loadCategoriesPort;
    private final CheckCategoryPort checkCategoryPort;
    private final DeleteCategoryPort deleteCategoryPort;

    public CategoryService(SaveCategoryPort saveCategoryPort,
                           LoadCategoriesPort loadCategoriesPort,
                           CheckCategoryPort checkCategoryPort,
                           DeleteCategoryPort deleteCategoryPort){
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
        if (!checkCategoryPort.existsById(id)){
            throw  new CategoryNotFoundException("Category does not exists");
        }
        deleteCategoryPort.deleteById(id);
    }
}
