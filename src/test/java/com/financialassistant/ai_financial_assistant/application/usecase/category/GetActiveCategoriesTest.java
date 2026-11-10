package com.financialassistant.ai_financial_assistant.application.usecase.category;

import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetActiveCategoriesTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private GetActiveCategories getActiveCategories;

    @Test
    @DisplayName("Should return only active categories")
    void shouldReturnOnlyActiveCategories() {

        List<Category> activeCategories = List.of(
                new Category("Food", TransactionType.EXPENSE, false),
                new Category("Salary", TransactionType.INCOME, false)
        );

        when(categoryRepository.findAllActive())
                .thenReturn(activeCategories);

        List<Category> result = getActiveCategories.execute();

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Category::isActive));

        verify(categoryRepository).findAllActive();
    }

    @Test
    @DisplayName("Should return an empty list when no active categories exist")
    void shouldReturnEmptyListWhenNoActiveCategoriesExist() {

        when(categoryRepository.findAllActive())
                .thenReturn(List.of());

        List<Category> result = getActiveCategories.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(categoryRepository).findAllActive();
    }
}