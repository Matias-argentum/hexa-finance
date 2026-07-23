package com.mat.hexa_finance.application.service.category;

import com.mat.hexa_finance.application.ports.in.category.*;
import com.mat.hexa_finance.application.ports.in.category.command.CreateCategoryCommand;
import com.mat.hexa_finance.application.ports.in.category.command.UpdateCategoryCommand;
import com.mat.hexa_finance.application.ports.out.category.CheckCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.DeleteCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.LoadCategoriesPort;
import com.mat.hexa_finance.application.ports.out.category.SaveCategoryPort;
import com.mat.hexa_finance.domain.exception.AccessDeniedException;
import com.mat.hexa_finance.domain.exception.CategoryNotFoundException;
import com.mat.hexa_finance.domain.model.Category;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public void delete(Long id, Long userId, boolean isAdmin) {
        Category category = loadCategoriesPort.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Category does not exist")
        );
        if (!isAdmin && (category.getUserId() == null || !category.getUserId().equals(userId))) {
            throw new AccessDeniedException("No permission granted");
        }
        deleteCategoryPort.deleteById(id);
    }

    @Override
    public Category update(UpdateCategoryCommand command, boolean isAdmin) {
        Category foundCategory = loadCategoriesPort.findById(command.getId())
                .orElseThrow(() -> new CategoryNotFoundException("Category does not exist"));
        if (!isAdmin && (foundCategory.getUserId() == null || !foundCategory.getUserId().equals(command.getUserId()))) {
            throw new AccessDeniedException("No permission granted");
        }
        foundCategory.setName(command.getName());
        foundCategory.setIconName(command.getIconName());
        foundCategory.setUserId(command.getUserId());

        return saveCategoryPort.save(foundCategory);
    }

    @Override
    public Category execute(Long id) {
        return loadCategoriesPort.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category does not exist"));
    }

    @Override
    public List<Category> executeSearch(Long userId) {
        return loadCategoriesPort.findAllByUserId(userId).stream().sorted(Comparator.comparing(Category::getName)).collect(Collectors.toList());
    }
}
