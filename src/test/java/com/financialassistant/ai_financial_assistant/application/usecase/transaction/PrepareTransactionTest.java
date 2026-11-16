package com.financialassistant.ai_financial_assistant.application.usecase.transaction;

import com.financialassistant.ai_financial_assistant.application.dto.TransactionPreview;
import com.financialassistant.ai_financial_assistant.application.exception.CategoryNotFoundException;
import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrepareTransactionTest {

    @Mock
    private CategoryRepository categoryRepository;

    private PrepareTransaction prepareTransaction;

    @BeforeEach
    void setUp() {
        prepareTransaction = new PrepareTransaction(categoryRepository);
    }

    @Test
    void shouldPrepareTransactionSuccessfully() {
        UUID categoryId = UUID.randomUUID();

        Category category = new Category(
                "Salary",
                TransactionType.INCOME,
                false
        );

        LocalDate transactionDate = LocalDate.of(2026, 11, 12);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        TransactionPreview result = prepareTransaction.execute(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                transactionDate
        );

        assertThat(result.description()).isEqualTo("Monthly salary");
        assertThat(result.amount()).isEqualByComparingTo("3500.00");
        assertThat(result.categoryId()).isEqualTo(category.getId());
        assertThat(result.categoryName()).isEqualTo("Salary");
        assertThat(result.type()).isEqualTo("INCOME");
        assertThat(result.transactionDate()).isEqualTo(transactionDate);

        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void shouldThrowExceptionWhenCategoryDoesNotExist() {
        UUID categoryId = UUID.randomUUID();

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> prepareTransaction.execute(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                LocalDate.of(2026, 11, 12)
        )).isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("Category not found: " + categoryId);

        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void shouldThrowExceptionWhenAmountIsInvalid() {
        UUID categoryId = UUID.randomUUID();

        Category category = new Category(
                "Salary",
                TransactionType.INCOME,
                false
        );

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        assertThatThrownBy(() -> prepareTransaction.execute(
                "Monthly salary",
                BigDecimal.ZERO,
                categoryId,
                LocalDate.of(2026, 11, 12)
        )).isInstanceOf(RuntimeException.class);

        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void shouldThrowExceptionWhenCategoryIsInactive() {
        UUID categoryId = UUID.randomUUID();

        Category category = new Category(
                "Salary",
                TransactionType.INCOME,
                false
        );
        category.deactivate();

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        assertThatThrownBy(() -> prepareTransaction.execute(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                LocalDate.of(2026, 11, 12)
        )).isInstanceOf(RuntimeException.class);

        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }
}