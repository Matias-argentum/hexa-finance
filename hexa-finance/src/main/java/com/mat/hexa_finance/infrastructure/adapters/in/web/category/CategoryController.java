package com.mat.hexa_finance.infrastructure.adapters.in.web.category;

import com.mat.hexa_finance.application.ports.in.category.SaveCategoryUseCase;
import com.mat.hexa_finance.application.ports.in.category.command.CreateCategoryCommand;
import com.mat.hexa_finance.domain.model.Category;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.category.dto.CategoryResponse;
import com.mat.hexa_finance.infrastructure.adapters.out.persistence.category.mapper.CategoryWebMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {

    private final SaveCategoryUseCase saveCategoryUseCase;

    public CategoryController(SaveCategoryUseCase saveCategoryUseCase){
        this.saveCategoryUseCase = saveCategoryUseCase;
    }

    @GetMapping("/")
    public String showIndex(){
        return  "index";
    }

    @PostMapping("/categories")
    public String createCategory(@RequestParam String name, @RequestParam String iconName, Model model){
        CreateCategoryCommand command = new CreateCategoryCommand(name, iconName, null);
        CategoryResponse savedCategory = CategoryWebMapper.toResponse(saveCategoryUseCase.execute(command));
        model.addAttribute("category", savedCategory);
        return "fragments/category-item";
    }
}
