package com.financialassistant.ai_financial_assistant.application.usecase.transaction;

import com.financialassistant.ai_financial_assistant.application.dto.PendingTransactionPreview;
import com.financialassistant.ai_financial_assistant.application.dto.TransactionPreview;
import com.financialassistant.ai_financial_assistant.application.exception.PendingTransactionPreviewNotFoundException;
import com.financialassistant.ai_financial_assistant.application.port.PendingTransactionStore;
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
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmTransactionTest {

    @Mock
    private PendingTransactionStore pendingTransactionStore;

    @Mock
    private CreateFinancialTransaction createFinancialTransaction;

    private ConfirmTransaction confirmTransaction;

    @BeforeEach
    void setUp() {
        confirmTransaction = new ConfirmTransaction(
                pendingTransactionStore,
                createFinancialTransaction
        );
    }

    @Test
    void shouldConfirmTransactionSuccessfully() {
        UUID previewId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        TransactionPreview preview = new TransactionPreview(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                "Salary",
                "INCOME",
                LocalDate.of(2026, 11, 12)
        );

        PendingTransactionPreview pendingPreview =
                new PendingTransactionPreview(
                        previewId,
                        preview,
                        Instant.now().plusSeconds(600)
                );

        Category category = new Category(
                "Salary",
                TransactionType.INCOME,
                false
        );

        FinancialTransaction expectedTransaction =
                new FinancialTransaction(
                        "Monthly salary",
                        new Money(new BigDecimal("3500.00")),
                        category,
                        LocalDate.of(2026, 11, 12)
                );

        when(pendingTransactionStore.consume(
                org.mockito.ArgumentMatchers.eq(previewId),
                org.mockito.ArgumentMatchers.any(Instant.class)
        )).thenReturn(Optional.of(pendingPreview));

        when(createFinancialTransaction.execute(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                LocalDate.of(2026, 11, 12)
        )).thenReturn(expectedTransaction);

        FinancialTransaction result = confirmTransaction.execute(previewId);

        assertThat(result).isEqualTo(expectedTransaction);

        verify(pendingTransactionStore).consume(
                org.mockito.ArgumentMatchers.eq(previewId),
                org.mockito.ArgumentMatchers.any(Instant.class)
        );

        verify(createFinancialTransaction).execute(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                LocalDate.of(2026, 11, 12)
        );
    }

    @Test
    void shouldThrowExceptionWhenPreviewDoesNotExist() {
        UUID previewId = UUID.randomUUID();

        when(pendingTransactionStore.consume(
                org.mockito.ArgumentMatchers.eq(previewId),
                org.mockito.ArgumentMatchers.any(Instant.class)
        )).thenReturn(Optional.empty());

        assertThatThrownBy(() -> confirmTransaction.execute(previewId))
                .isInstanceOf(PendingTransactionPreviewNotFoundException.class)
                .hasMessageContaining(previewId.toString());

        verify(pendingTransactionStore).consume(
                org.mockito.ArgumentMatchers.eq(previewId),
                org.mockito.ArgumentMatchers.any(Instant.class)
        );

        verifyNoInteractions(createFinancialTransaction);
    }

    @Test
    void shouldNotCreateTransactionWhenPreviewIsExpired() {
        UUID previewId = UUID.randomUUID();

        when(pendingTransactionStore.consume(
                org.mockito.ArgumentMatchers.eq(previewId),
                org.mockito.ArgumentMatchers.any(Instant.class)
        )).thenReturn(Optional.empty());

        assertThatThrownBy(() -> confirmTransaction.execute(previewId))
                .isInstanceOf(PendingTransactionPreviewNotFoundException.class);

        verifyNoInteractions(createFinancialTransaction);
    }

    @Test
    void shouldPropagateExceptionWhenTransactionCreationFails() {
        UUID previewId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        TransactionPreview preview = new TransactionPreview(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                "Salary",
                "INCOME",
                LocalDate.of(2026, 11, 12)
        );

        PendingTransactionPreview pendingPreview =
                new PendingTransactionPreview(
                        previewId,
                        preview,
                        Instant.now().plusSeconds(600)
                );

        when(pendingTransactionStore.consume(
                org.mockito.ArgumentMatchers.eq(previewId),
                org.mockito.ArgumentMatchers.any(Instant.class)
        )).thenReturn(Optional.of(pendingPreview));

        when(createFinancialTransaction.execute(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                LocalDate.of(2026, 11, 12)
        )).thenThrow(new IllegalArgumentException("Invalid transaction"));

        assertThatThrownBy(() -> confirmTransaction.execute(previewId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid transaction");

        verify(createFinancialTransaction).execute(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                LocalDate.of(2026, 11, 12)
        );
    }
}