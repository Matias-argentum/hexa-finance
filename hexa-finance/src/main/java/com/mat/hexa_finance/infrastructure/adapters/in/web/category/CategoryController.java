package com.mat.hexa_finance.infrastructure.adapters.in.web.category;

import com.mat.hexa_finance.application.ports.in.category.*;
import com.mat.hexa_finance.application.ports.in.category.command.CreateCategoryCommand;
import com.mat.hexa_finance.application.ports.in.category.command.UpdateCategoryCommand;
import com.mat.hexa_finance.domain.model.Category;
import com.mat.hexa_finance.infrastructure.adapters.in.web.category.dto.CategoryResponse;
import com.mat.hexa_finance.infrastructure.adapters.in.web.category.mapper.CategoryWebMapper;
import com.mat.hexa_finance.infrastructure.adapters.in.web.common.utils.HtmxUtils.HtmxUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
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
    private final GetCategoryUseCase getCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;

    public CategoryController(SaveCategoryUseCase saveCategoryUseCase,
                              FindAllCategoriesUseCase findAllCategoriesUseCase,
                              DeleteCategoryUseCase deleteCategoryUseCase,
                              GetCategoryUseCase getCategoryUseCase,
                              UpdateCategoryUseCase updateCategoryUseCase
    ) {
        this.saveCategoryUseCase = saveCategoryUseCase;
        this.findAllCategoriesUseCase = findAllCategoriesUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.getCategoryUseCase = getCategoryUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
    }

    @GetMapping("/categories")
    public String getCategories(@RequestParam(required = false) boolean login, Model model) {
        if (login) {
            List<Category> categoriesList = findAllCategoriesUseCase.execute();
            List<CategoryResponse> categoriesResponseList = categoriesList.stream()
                    .map(CategoryWebMapper::toResponse)
                    .collect(Collectors.toList());

            model.addAttribute("categories", categoriesResponseList);

            model.addAttribute("content", "categories :: categories-content");
        } else {
            model.addAttribute("content", "pages/welcome :: welcome-content");
        }
        return "layouts/main-layout";
    }


    @GetMapping("/")
    public String showIndex(@RequestParam(required = false) boolean login, Model model) {
        if (login) {
            model.addAttribute("content", "index :: dashboard-content");
        } else {
            model.addAttribute("content", "pages/welcome :: welcome-content");
        }


        return "layouts/main-layout";
    }

    @GetMapping("/categories/{id}")
    public String getCategory(@PathVariable Long id, Model model) {
        CategoryResponse retrievedCategory = CategoryWebMapper.toResponse(getCategoryUseCase.execute(id));
        model.addAttribute("category", retrievedCategory); //No sé si esta bien
        return "fragments/category-fragments :: category-item"; //no sé si está bien
    }

    @GetMapping("/categories/{id}/edit")
    public String getCategoryForm(@PathVariable Long id, Model model) {
        CategoryResponse retrievedCategory = CategoryWebMapper.toResponse(getCategoryUseCase.execute(id));
        model.addAttribute("category", retrievedCategory); //No sé si esta bien
        return "fragments/category-fragments :: category-edit-form"; //no sé si está bien
    }

    @PutMapping("/categories/{id}")
    public String updateCategory(@PathVariable Long id, @RequestParam String name,
                                 @RequestParam String iconName,
                                 Model model,
                                 HttpServletResponse response) {
        UpdateCategoryCommand updateCommand = new UpdateCategoryCommand(id, name, iconName, null);
        CategoryResponse updatedCategory = CategoryWebMapper.toResponse(updateCategoryUseCase.update(updateCommand));

        model.addAttribute("category", updatedCategory);
        HtmxUtils.addNotification(response, "Categoría actualizada!");

        return "fragments/category-fragments :: category-item";
    }

    @PostMapping("/categories")
    public ResponseEntity<Void> createCategory(
            @RequestParam String name,
            @RequestParam String iconName,
            HttpServletResponse response) {

        CreateCategoryCommand command = new CreateCategoryCommand(name, iconName, null);
        CategoryResponse savedCategory = CategoryWebMapper.toResponse(saveCategoryUseCase.execute(command));

        HtmxUtils.addNotificationAndClose(response, "Categoría " + savedCategory.name() + " creada!");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/categories/new")
    public String getCreateCategoryModal(Model model) {
        return "fragments/category-fragments :: category-modal";
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id, HttpServletResponse response) {
        deleteCategoryUseCase.delete(id);
        HtmxUtils.addNotificationAndClose(response, "Categoría eliminada!");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/categories/{id}/delete-confirm")
    public String getDeleteCategoryModalAlert(@PathVariable Long id,  Model model){
        CategoryResponse retrievedCategory = CategoryWebMapper.toResponse(getCategoryUseCase.execute(id));
        model.addAttribute("category", retrievedCategory);
        return "fragments/category-fragments :: category-alert-modal";
    }
}
