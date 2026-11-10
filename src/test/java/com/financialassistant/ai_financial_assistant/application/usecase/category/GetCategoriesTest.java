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
class GetCategoriesTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private GetCategories getCategories;

    @Test
    @DisplayName("Should return all categories")
    void shouldReturnAllCategories() {

        List<Category> categories = List.of(
                new Category("Food", TransactionType.EXPENSE, false),
                new Category("Salary", TransactionType.INCOME, false)
        );

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = getCategories.execute();

        assertEquals(2, result.size());
        assertEquals("Food", result.get(0).getName());
        assertEquals("Salary", result.get(1).getName());

        verify(categoryRepository).findAll();
    }

    @Test
    @DisplayName("Should return an empty list when no categories exist")
    void shouldReturnEmptyListWhenNoCategoriesExist() {

        when(categoryRepository.findAll()).thenReturn(List.of());

        List<Category> result = getCategories.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(categoryRepository).findAll();
    }

}