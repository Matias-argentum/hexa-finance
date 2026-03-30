package com.mat.hexa_finance.infrastructure.adapters.out.persistence.category;

import com.mat.hexa_finance.application.ports.out.category.CheckCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.DeleteCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.LoadCategoriesPort;
import com.mat.hexa_finance.application.ports.out.category.SaveCategoryPort;
import com.mat.hexa_finance.domain.model.Category;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.user.JpaUserRepository;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.user.UserEntity;
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
    private final JpaUserRepository userRepository;

    public CategoryPersisteneceAdapter(JpaCategoryRepository repository, JpaUserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        entity.setIconName(category.getIconName());

        if (category.getUserId() != null) {
            UserEntity ownerRef = userRepository.getReferenceById(category.getUserId());
            entity.setOwner(ownerRef);
        }
        // si es null owner queda null y es categoría global

        CategoryEntity saved = repository.save(entity);

        Long savedOwnerId = saved.getOwner() != null ? saved.getOwner().getId() : null;

        return new Category(saved.getId(), saved.getName(), saved.getIconName(), savedOwnerId);
    }

    @Override
    public List<Category> findAllByUserId(Long userId) {
        return repository.findAllGlobalAndByUserId(userId)
                .stream()
                .map(entity -> {
                    Long ownerId = entity.getOwner() != null ? entity.getOwner().getId() : null;
                    return new Category(
                            entity.getId(),
                            entity.getName(),
                            entity.getIconName(),
                            ownerId
                    );
                })
                .toList();
    }

    @Override
    public Optional<Category> findById(Long id) {
        Optional<CategoryEntity> foundCategory = repository.findById(id);
        return foundCategory.map(entity -> {
            Long ownerId = entity.getOwner() != null ? entity.getOwner().getId() : null;
            return new Category(
                    entity.getId(),
                    entity.getName(),
                    entity.getIconName(),
                    ownerId
            );
        });
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
