package com.mat.hexa_finance.application.ports.in.category;

import com.mat.hexa_finance.application.ports.in.category.command.CreateCategoryCommand;
import com.mat.hexa_finance.domain.model.Category;

public interface SaveCategoryUseCase {
    Category execute(CreateCategoryCommand command);
}
