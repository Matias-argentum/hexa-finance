package com.mat.hexa_finance.application.ports.in.category;

public interface DeleteCategoryUseCase {
    void delete(Long id, Long userId, boolean isAdmin);
}
