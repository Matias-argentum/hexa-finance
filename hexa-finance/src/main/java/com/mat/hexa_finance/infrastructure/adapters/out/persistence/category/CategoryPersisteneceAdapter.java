package com.mat.hexa_finance.infrastructure.adapters.out.persistence.category;

import com.mat.hexa_finance.application.ports.out.category.CheckCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.DeleteCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.LoadCategoriesPort;
import com.mat.hexa_finance.application.ports.out.category.SaveCategoryPort;
import com.mat.hexa_finance.domain.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoryPersisteneceAdapter implements
        SaveCategoryPort,
        LoadCategoriesPort,
        CheckCategoryPort,
        DeleteCategoryPort {

    private final JpaCategoryRepository repository;

    public CategoryPersisteneceAdapter(JpaCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        entity.setIconName(category.getIconName());
        entity.setUserId(category.getUserId());

        CategoryEntity savedEntity = repository.save(entity);

        return new Category(savedEntity.getId(), savedEntity.getName(), savedEntity.getIconName(), savedEntity.getUserId());

    }

    @Override
    public List<Category> findAll() {
        List<CategoryEntity> foundCategories = repository.findAll();
        return foundCategories.stream()
                .map(c -> new Category(c.getId(), c.getName(), c.getIconName(), c.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> findById(Long id) {
        Optional<CategoryEntity> foundCategory = repository.findById(id);
        return foundCategory.map(c -> new Category(c.getId(), c.getName(), c.getIconName(), c.getUserId()));
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
