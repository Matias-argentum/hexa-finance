package com.mat.hexa_finance.infrastructure.config;

import com.mat.hexa_finance.application.ports.in.category.FindAllCategoriesUseCase;
import com.mat.hexa_finance.application.ports.in.category.SaveCategoryUseCase;
import com.mat.hexa_finance.application.ports.out.category.LoadCategoriesPort;
import com.mat.hexa_finance.application.ports.out.category.SaveCategoryPort;
import com.mat.hexa_finance.application.service.CategoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryBeanConfig {

    @Bean
    public CategoryService categoryService(SaveCategoryPort savePort, LoadCategoriesPort loadPort) {
        return new CategoryService(savePort, loadPort);
    }
}

