package com.mat.hexa_finance.application.ports.in.category;

import com.mat.hexa_finance.application.ports.in.category.command.UpdateCategoryCommand;
import com.mat.hexa_finance.domain.model.Category;

public interface UpdateCategoryUseCase {
    Category update(UpdateCategoryCommand command, boolean isAdmin);
}
