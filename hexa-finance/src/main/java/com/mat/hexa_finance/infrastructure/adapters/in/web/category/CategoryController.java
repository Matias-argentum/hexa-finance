package com.mat.hexa_finance.infrastructure.adapters.in.web.category;

import com.mat.hexa_finance.application.ports.in.category.*;
import com.mat.hexa_finance.application.ports.in.category.command.CreateCategoryCommand;
import com.mat.hexa_finance.application.ports.in.category.command.UpdateCategoryCommand;
import com.mat.hexa_finance.domain.model.Category;
import com.mat.hexa_finance.infrastructure.adapters.in.web.auth.model.CustomUserDetails;
import com.mat.hexa_finance.infrastructure.adapters.in.web.category.dto.CategoryResponse;
import com.mat.hexa_finance.infrastructure.adapters.in.web.category.mapper.CategoryWebMapper;
import com.mat.hexa_finance.infrastructure.adapters.in.web.common.utils.HtmxUtils.HtmxUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("/test")
    public void test(@AuthenticationPrincipal CustomUserDetails user) {
        System.out.println("ID del usuario logueado: " + user.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/categories")
    public String getCategories(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        Long userId = user.getId();
        List<Category> categoriesList = findAllCategoriesUseCase.executeSearch(userId);
        List<CategoryResponse> categoriesResponseList = categoriesList.stream()
                .map(CategoryWebMapper::toResponse)
                .collect(Collectors.toList());

        model.addAttribute("categories", categoriesResponseList);

        model.addAttribute("content", "categories/categories :: categories-content");

        return "layouts/main-layout";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/categories/{id}")
    public String getCategory(@PathVariable Long id, Model model) {
        CategoryResponse retrievedCategory = CategoryWebMapper.toResponse(getCategoryUseCase.execute(id));
        model.addAttribute("category", retrievedCategory); //No sé si esta bien
        return "categories/fragments/category-fragments :: category-item"; //no sé si está bien
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/categories/{id}/edit")
    public String getCategoryForm(@PathVariable Long id, Model model) {
        CategoryResponse retrievedCategory = CategoryWebMapper.toResponse(getCategoryUseCase.execute(id));
        model.addAttribute("category", retrievedCategory); //No sé si esta bien
        return "categories/fragments/category-fragments :: category-edit-form"; //no sé si está bien
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/categories/{id}")
    public String updateCategory(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long id, @RequestParam String name,
                                 @RequestParam String iconName,
                                 Model model,
                                 HttpServletResponse response) {

        Long userId = user.isAdmin() ? null : user.getId();

        UpdateCategoryCommand updateCommand = new UpdateCategoryCommand(id, name, iconName, userId);
        CategoryResponse updatedCategory = CategoryWebMapper.toResponse(updateCategoryUseCase.update(updateCommand, user.isAdmin()));

        model.addAttribute("category", updatedCategory);
        HtmxUtils.addNotification(response, "Categoría actualizada!");

        return "categories/fragments/category-fragments :: category-item";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/categories")
    public ResponseEntity<Void> createCategory(
            @RequestParam String name,
            @RequestParam String iconName,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletResponse response) {

        Long ownerUserId = userDetails.isAdmin() ? null : userDetails.getId();

        CreateCategoryCommand command = new CreateCategoryCommand(name, iconName, ownerUserId);
        CategoryResponse savedCategory = CategoryWebMapper.toResponse(saveCategoryUseCase.execute(command));

        HtmxUtils.addNotificationAndClose(response, "Categoría " + savedCategory.name() + " creada!");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/categories/new")
    public String getCreateCategoryModal(Model model) {
        return "categories/fragments/category-fragments :: category-modal";
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id,
                                               @AuthenticationPrincipal CustomUserDetails user,
                                               HttpServletResponse response) {
        deleteCategoryUseCase.delete(id, user.getId(), user.isAdmin());
        HtmxUtils.addNotificationAndClose(response, "Categoría eliminada!");
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/categories/{id}/delete-confirm")
    public String getDeleteCategoryModalAlert(@PathVariable Long id,
                                              @AuthenticationPrincipal CustomUserDetails user,
                                              Model model) {

        Category retrievedCategory = getCategoryUseCase.execute(id);
        if (!user.isAdmin() && retrievedCategory.getUserId() == null){
            throw new AccessDeniedException("Access denied, category not owned by user");
        }

        CategoryResponse categoryResponse = CategoryWebMapper.toResponse(retrievedCategory);
        model.addAttribute("category", categoryResponse);
        return "categories/fragments/category-fragments :: category-alert-modal";
    }
}
