package com.mat.hexa_finance.infrastructure.adapters.in.web.category.dto;

public record CategoryResponse(
        Long id,
        String name,
        Long userId,
        String iconName
) {
}
