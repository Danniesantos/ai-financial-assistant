package com.financialassistant.ai_financial_assistant.infrastructure.ai.tools;

import com.financialassistant.ai_financial_assistant.application.usecase.category.GetActiveCategories;
import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryCategoriesToolTest {

    @Mock
    private GetActiveCategories getActiveCategories;

    private QueryCategoriesTool queryCategoriesTool;

    @BeforeEach
    void setUp() {
        queryCategoriesTool = new QueryCategoriesTool(getActiveCategories);
    }

    @Test
    void shouldReturnCategoryData() {
        Category salary = new Category(
                "Salary",
                TransactionType.INCOME,
                true
        );

        Category food = new Category(
                "Food",
                TransactionType.EXPENSE,
                false
        );

        when(getActiveCategories.execute())
                .thenReturn(List.of(salary, food));

        List<QueryCategoriesTool.CategoryData> result =
                queryCategoriesTool.getCategories();

        assertThat(result).hasSize(2);

        assertThat(result.get(0).id())
                .isEqualTo(salary.getId().toString());
        assertThat(result.get(0).name()).isEqualTo("Salary");
        assertThat(result.get(0).type()).isEqualTo("INCOME");
        assertThat(result.get(0).isDefault()).isTrue();

        assertThat(result.get(1).id())
                .isEqualTo(food.getId().toString());
        assertThat(result.get(1).name()).isEqualTo("Food");
        assertThat(result.get(1).type()).isEqualTo("EXPENSE");
        assertThat(result.get(1).isDefault()).isFalse();

        verify(getActiveCategories).execute();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoActiveCategories() {
        when(getActiveCategories.execute()).thenReturn(List.of());

        List<QueryCategoriesTool.CategoryData> result =
                queryCategoriesTool.getCategories();

        assertThat(result).isEmpty();

        verify(getActiveCategories).execute();
    }
}