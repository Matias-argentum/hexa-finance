package com.mat.hexa_finance.infrastructure.config;

import com.mat.hexa_finance.application.ports.in.category.SaveCategoryUseCase;
import com.mat.hexa_finance.application.ports.out.category.SaveCategoryPort;
import com.mat.hexa_finance.application.service.CategoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryBeanConfig {
    @Bean
    public SaveCategoryUseCase saveCategoryUseCase(SaveCategoryPort saveCategoryPort) {
        return new CategoryService(saveCategoryPort);
    }
}

