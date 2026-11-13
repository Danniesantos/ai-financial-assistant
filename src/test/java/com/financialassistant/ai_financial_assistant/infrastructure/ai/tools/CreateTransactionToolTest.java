package com.financialassistant.ai_financial_assistant.infrastructure.ai.tools;

import com.financialassistant.ai_financial_assistant.application.usecase.transaction.CreateFinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTransactionToolTest {

    @Mock
    private CreateFinancialTransaction createFinancialTransaction;

    private CreateTransactionTool createTransactionTool;

    @BeforeEach
    void setUp() {
        createTransactionTool = new CreateTransactionTool(
                createFinancialTransaction
        );
    }

    @Test
    void shouldCreateTransactionSuccessfully() {
        UUID categoryId = UUID.randomUUID();

        Category category = new Category(
                "Salary",
                TransactionType.INCOME,
                false
        );

        FinancialTransaction transaction = new FinancialTransaction(
                "Monthly salary",
                new Money(new BigDecimal("3500.00")),
                category,
                LocalDate.of(2026, 11, 5)
        );

        when(createFinancialTransaction.execute(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                LocalDate.of(2026, 11, 5)
        )).thenReturn(transaction);

        CreateTransactionTool.TransactionCreationResult result =
                createTransactionTool.createTransaction(
                        "Monthly salary",
                        new BigDecimal("3500.00"),
                        categoryId.toString(),
                        "2026-11-05"
                );

        assertThat(result.id()).isEqualTo(transaction.getId().toString());
        assertThat(result.description()).isEqualTo("Monthly salary");
        assertThat(result.amount()).isEqualByComparingTo("3500.00");
        assertThat(result.category()).isEqualTo("Salary");
        assertThat(result.type()).isEqualTo("INCOME");
        assertThat(result.transactionDate())
                .isEqualTo(LocalDate.of(2026, 11, 5));
        assertThat(result.active()).isTrue();

        verify(createFinancialTransaction).execute(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                LocalDate.of(2026, 11, 5)
        );
    }

    @Test
    void shouldThrowExceptionWhenCategoryIdIsInvalid() {
        assertThatThrownBy(() ->
                createTransactionTool.createTransaction(
                        "Monthly salary",
                        new BigDecimal("3500.00"),
                        "invalid-uuid",
                        "2026-11-05"
                )
        ).isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(createFinancialTransaction);
    }

    @Test
    void shouldThrowExceptionWhenTransactionDateIsInvalid() {
        UUID categoryId = UUID.randomUUID();

        assertThatThrownBy(() ->
                createTransactionTool.createTransaction(
                        "Monthly salary",
                        new BigDecimal("3500.00"),
                        categoryId.toString(),
                        "invalid-date"
                )
        ).isInstanceOf(java.time.format.DateTimeParseException.class);

        verifyNoInteractions(createFinancialTransaction);
    }
}