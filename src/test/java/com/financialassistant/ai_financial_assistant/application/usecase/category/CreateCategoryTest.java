package com.financialassistant.ai_financial_assistant.application.usecase.category;

import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.exception.InvalidCategoryException;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCategoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CreateCategory createCategory;

    @Test
    @DisplayName("Should create a category successfully")
    void shouldCreateCategorySuccessfully() {

        String name = "Food";
        TransactionType type = TransactionType.EXPENSE;
        boolean isDefault = false;

        Category expectedCategory = new Category(
                name,
                type,
                isDefault
        );

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(expectedCategory);

        Category result = createCategory.execute(
                name,
                type,
                isDefault
        );

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(type, result.getType());
        assertEquals(isDefault, result.isDefault());
        assertTrue(result.isActive());

        ArgumentCaptor<Category> captor =
                ArgumentCaptor.forClass(Category.class);

        verify(categoryRepository).save(captor.capture());

        Category savedCategory = captor.getValue();

        assertEquals(name, savedCategory.getName());
        assertEquals(type, savedCategory.getType());
        assertEquals(isDefault, savedCategory.isDefault());
    }

    @Test
    @DisplayName("Should not save category when name is invalid")
    void shouldNotSaveCategoryWhenNameIsInvalid() {

        assertThrows(
                InvalidCategoryException.class,
                () -> createCategory.execute(
                        " ",
                        TransactionType.EXPENSE,
                        false
                )
        );

        verify(categoryRepository, never())
                .save(any(Category.class));
    }

    @Test
    @DisplayName("Should not save category when type is null")
    void shouldNotSaveCategoryWhenTypeIsNull() {

        assertThrows(
                InvalidCategoryException.class,
                () -> createCategory.execute(
                        "Food",
                        null,
                        false
                )
        );

        verify(categoryRepository, never())
                .save(any(Category.class));
    }

}