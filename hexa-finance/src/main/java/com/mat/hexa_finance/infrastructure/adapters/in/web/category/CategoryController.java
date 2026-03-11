package com.mat.hexa_finance.infrastructure.adapters.in.web.category;

import com.mat.hexa_finance.application.ports.in.category.DeleteCategoryUseCase;
import com.mat.hexa_finance.application.ports.in.category.FindAllCategoriesUseCase;
import com.mat.hexa_finance.application.ports.in.category.SaveCategoryUseCase;
import com.mat.hexa_finance.application.ports.in.category.command.CreateCategoryCommand;
import com.mat.hexa_finance.domain.model.Category;
import com.mat.hexa_finance.infrastructure.adapters.in.web.category.dto.CategoryResponse;
import com.mat.hexa_finance.infrastructure.adapters.in.web.category.mapper.CategoryWebMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CategoryController {

    private final SaveCategoryUseCase saveCategoryUseCase;
    private final FindAllCategoriesUseCase findAllCategoriesUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(SaveCategoryUseCase saveCategoryUseCase,
                              FindAllCategoriesUseCase findAllCategoriesUseCase,
                              DeleteCategoryUseCase deleteCategoryUseCase) {
        this.saveCategoryUseCase = saveCategoryUseCase;
        this.findAllCategoriesUseCase = findAllCategoriesUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
    }

    @GetMapping("/")
    public String showIndex(@RequestParam(required = false) boolean login, Model model) {
        if (login) {
            List<Category> categoriesList = findAllCategoriesUseCase.execute();
            List<CategoryResponse> categoriesResponseList = categoriesList.stream()
                    .map(CategoryWebMapper::toResponse)
                    .collect(Collectors.toList());

            model.addAttribute("categories", categoriesResponseList);

            model.addAttribute("content", "index :: dashboard-content");
        } else {
            model.addAttribute("content", "pages/welcome :: welcome-content");
        }


        return "layouts/main-layout";
    }

    @PostMapping("/categories")
    public String createCategory(@RequestParam String name, @RequestParam String iconName, Model model) {
        CreateCategoryCommand command = new CreateCategoryCommand(name, iconName, null);
        CategoryResponse savedCategory = CategoryWebMapper.toResponse(saveCategoryUseCase.execute(command));
        model.addAttribute("category", savedCategory);
        return "fragments/category-fragments :: category-item";
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        deleteCategoryUseCase.delete(id);
        return ResponseEntity.ok().build();
    }
}
