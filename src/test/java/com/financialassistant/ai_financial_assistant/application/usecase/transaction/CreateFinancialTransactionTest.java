package com.financialassistant.ai_financial_assistant.application.usecase.transaction;


import com.financialassistant.ai_financial_assistant.application.exception.CategoryNotFoundException;
import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.repository.CategoryRepository;
import com.financialassistant.ai_financial_assistant.domain.repository.FinancialTransactionRepository;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateFinancialTransactionTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private FinancialTransactionRepository transactionRepository;

    @InjectMocks
    private CreateFinancialTransaction createFinancialTransaction;

    @Test
    @DisplayName("Should create a financial transaction successfully")
    void shouldCreateFinancialTransactionSuccessfully() {

        Category category = new Category(
                "Food",
                TransactionType.EXPENSE,
                false
        );

        BigDecimal amount = new BigDecimal("150.00");
        String description = "Supermarket purchase";
        UUID categoryId = category.getId();
        LocalDate transactionDate = LocalDate.of(2026, 11, 3);

        FinancialTransaction expectedTransaction = new FinancialTransaction(
                description,
                new Money(amount),
                category,
                transactionDate
        );

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(transactionRepository.save(any(FinancialTransaction.class)))
                .thenReturn(expectedTransaction);


        FinancialTransaction result = createFinancialTransaction.execute(
                description,
                amount,
                categoryId,
                transactionDate
        );

        assertNotNull(result);
        assertEquals(description, result.getDescription());
        assertEquals(amount, result.getAmount().getAmount());
        assertEquals(categoryId, category.getId());
        assertEquals(transactionDate, result.getTransactionDate());

        verify(categoryRepository).findById(categoryId);
        verify(transactionRepository).save(any(FinancialTransaction.class));
    }

    @Test
    @DisplayName("Should throw exception when category does not exist")
    void shouldThrowExceptionWhenCategoryDoesNotExist() {

        UUID categoryId = UUID.randomUUID();

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> createFinancialTransaction.execute(
                        "Supermarket purchase",
                        new BigDecimal("150.00"),
                        categoryId,
                        LocalDate.of(2026, 11, 3)
                )
        );

        verify(categoryRepository).findById(categoryId);
    }

    @Test
    @DisplayName("Should not save transaction when category does not exist")
    void shouldNotSaveTransactionWhenCategoryDoesNotExist() {

        UUID categoryId = UUID.randomUUID();

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> createFinancialTransaction.execute(
                        "Supermarket purchase",
                        new BigDecimal("150.00"),
                        categoryId,
                        LocalDate.of(2026, 11, 3)
                )
        );

        verify(transactionRepository, never())
                .save(any(FinancialTransaction.class));
    }

}