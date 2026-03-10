package com.mat.hexa_finance.infrastructure.adapters.out.persistence.category;

import com.mat.hexa_finance.application.ports.out.category.SaveCategoryPort;
import com.mat.hexa_finance.domain.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryPersisteneceAdapter implements SaveCategoryPort {

    private final JpaCategoryRepository repository;

    public CategoryPersisteneceAdapter(JpaCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(category.getName());
        entity.setIconName(category.getIconName());
        entity.setUserId(category.getUserId());

        CategoryEntity savedEntity = repository.save(entity);

        return new Category(savedEntity.getId(), savedEntity.getName(), savedEntity.getIconName(), savedEntity.getUserId());

    }
}
