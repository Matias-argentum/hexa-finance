package com.mat.hexa_finance.infrastructure.config;

import com.mat.hexa_finance.application.ports.out.category.CheckCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.DeleteCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.LoadCategoriesPort;
import com.mat.hexa_finance.application.ports.out.category.SaveCategoryPort;
import com.mat.hexa_finance.application.service.category.CategoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryBeanConfig {

    @Bean
    public CategoryService categoryService(SaveCategoryPort savePort, LoadCategoriesPort loadPort, DeleteCategoryPort deletePort, CheckCategoryPort checkPort) {
        return new CategoryService(savePort, loadPort, checkPort, deletePort);
    }
}

