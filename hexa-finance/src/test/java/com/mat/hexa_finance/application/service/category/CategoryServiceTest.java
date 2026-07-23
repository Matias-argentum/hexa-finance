package com.mat.hexa_finance.application.service.category;

import com.mat.hexa_finance.application.ports.in.category.command.CreateCategoryCommand;
import com.mat.hexa_finance.application.ports.in.category.command.UpdateCategoryCommand;
import com.mat.hexa_finance.application.ports.out.category.CheckCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.DeleteCategoryPort;
import com.mat.hexa_finance.application.ports.out.category.LoadCategoriesPort;
import com.mat.hexa_finance.application.ports.out.category.SaveCategoryPort;
import com.mat.hexa_finance.domain.exception.AccessDeniedException;
import com.mat.hexa_finance.domain.exception.CategoryNotFoundException;
import com.mat.hexa_finance.domain.model.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(
        MockitoExtension.class
)
public class CategoryServiceTest {
    @Mock
    SaveCategoryPort saveCategoryPort;
    @Mock
    LoadCategoriesPort loadCategoriesPort;
    @Mock
    CheckCategoryPort checkCategoryPort;
    @Mock
    DeleteCategoryPort deleteCategoryPort;

    @InjectMocks
    CategoryService categoryService;

    @Test
    void shouldReturnCategoriesSortedAlphabeticallyByName() {
        var c1 = new Category(1L, "Ocio", "icon", 1L);
        var c2 = new Category(1L, "Sueldo", "icon", 1L);
        var c3 = new Category(1L, "Gastos Varios", "icon", 1L);
        var c4 = new Category(1L, "Servicios", "icon", 1L);

        when(loadCategoriesPort.findAllByUserId(1L))
                .thenReturn(List.of(c1, c2, c3, c4)); // aca estan desordenadas

        var result = categoryService.executeSearch(1L);

        Assertions.assertEquals(
                List.of("Gastos Varios", "Ocio", "Servicios", "Sueldo"),
                result.stream().map(Category::getName).toList()
        );
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoCategories(){
        when(loadCategoriesPort.findAllByUserId(1L))
                .thenReturn(List.of());

        var result = categoryService.executeSearch(1L);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void shouldSaveACategorySuccessfully() {
        // preparacion
        var created = new Category(1L, "Name", "Icon", 1L);
        var command = new CreateCategoryCommand("Name", "Icon", 1L);
        var captor = ArgumentCaptor.forClass(Category.class);

        // no importa que pasemos pero devolveme created
        when(saveCategoryPort.save(any(Category.class))).thenReturn(created);

        // actuamos
        var result = categoryService.execute(command);

        // verificamos y hacemos los asserts
        verify(saveCategoryPort).save(captor.capture());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());

        Assertions.assertEquals("Name", captor.getValue().getName());
        Assertions.assertEquals("Icon", captor.getValue().getIconName());
        Assertions.assertEquals(1L, captor.getValue().getUserId());
    }

    @Test
    void shouldThrowExceptionWhenDeletingACategoryThatIsOwnedByAnotherUser() {
        // preparacion
        var category = new Category(1L, "Name", "Icon", 99L);

        when(loadCategoriesPort.findById(1L))
                .thenReturn(Optional.of(category));
        // acto y verificacion
        Assertions.assertThrows(AccessDeniedException.class, ()-> {
            categoryService.delete(1L, 5L, false);
        });

        verify(deleteCategoryPort, times(0)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingACategoryThatIsOwnedByAnotherUser() {
        // preparacion
        var category = new Category(1L, "Name", "Icon", 99L);
        var command = new UpdateCategoryCommand(1L, "Name", "Icon", 5L);

        when(loadCategoriesPort.findById(1L))
                .thenReturn(Optional.of(category));
        // acto y verificacion
        Assertions.assertThrows(AccessDeniedException.class, ()-> {
            categoryService.update(command, false);
        });

        verify(saveCategoryPort, times(0)).save(any(Category.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingACategoryThatIsNotFound() {
        // preparacion
        var command = new UpdateCategoryCommand(1L, "Name", "Icon", 5L);

        when(loadCategoriesPort.findById(1L))
                .thenReturn(Optional.empty());
        // acto y verificacion
        Assertions.assertThrows(CategoryNotFoundException.class, ()-> {
            categoryService.update(command, false);
        });

        verify(saveCategoryPort, times(0)).save(any(Category.class));
    }


}
