package com.financialassistant.ai_financial_assistant.infrastructure.ai.tools;

import com.financialassistant.ai_financial_assistant.application.dto.PendingTransactionPreview;
import com.financialassistant.ai_financial_assistant.application.dto.TransactionPreview;
import com.financialassistant.ai_financial_assistant.application.usecase.transaction.CreateTransactionPreview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTransactionToolTest {

    @Mock
    private CreateTransactionPreview createTransactionPreview;

    private CreateTransactionTool createTransactionTool;

    @BeforeEach
    void setUp() {
        createTransactionTool = new CreateTransactionTool(
                createTransactionPreview
        );
    }

    @Test
    void shouldCreateTransactionPreviewSuccessfully() {
        UUID categoryId = UUID.randomUUID();
        UUID previewId = UUID.randomUUID();

        LocalDate transactionDate = LocalDate.of(2026, 11, 5);
        Instant expiresAt = Instant.parse("2026-11-05T12:10:00Z");

        TransactionPreview transaction = new TransactionPreview(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                "Salary",
                "INCOME",
                transactionDate
        );

        PendingTransactionPreview pendingPreview =
                new PendingTransactionPreview(
                        previewId,
                        transaction,
                        expiresAt
                );

        when(createTransactionPreview.execute(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                transactionDate
        )).thenReturn(pendingPreview);

        CreateTransactionTool.TransactionPreviewResult result =
                createTransactionTool.createTransaction(
                        "Monthly salary",
                        new BigDecimal("3500.00"),
                        categoryId.toString(),
                        "2026-11-05"
                );

        assertThat(result.previewId()).isEqualTo(previewId.toString());
        assertThat(result.description()).isEqualTo("Monthly salary");
        assertThat(result.amount()).isEqualByComparingTo("3500.00");
        assertThat(result.category()).isEqualTo("Salary");
        assertThat(result.type()).isEqualTo("INCOME");
        assertThat(result.transactionDate()).isEqualTo(transactionDate);
        assertThat(result.expiresAt()).isEqualTo(expiresAt);

        verify(createTransactionPreview).execute(
                "Monthly salary",
                new BigDecimal("3500.00"),
                categoryId,
                transactionDate
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

        verifyNoInteractions(createTransactionPreview);
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

        verifyNoInteractions(createTransactionPreview);
    }
}