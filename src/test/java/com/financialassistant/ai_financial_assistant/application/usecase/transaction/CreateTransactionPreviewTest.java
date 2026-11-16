package com.financialassistant.ai_financial_assistant.application.usecase.transaction;

import com.financialassistant.ai_financial_assistant.application.dto.PendingTransactionPreview;
import com.financialassistant.ai_financial_assistant.application.dto.TransactionPreview;
import com.financialassistant.ai_financial_assistant.application.port.PendingTransactionStore;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTransactionPreviewTest {

    @Mock
    private PrepareTransaction prepareTransaction;

    @Mock
    private PendingTransactionStore pendingTransactionStore;

    private CreateTransactionPreview createTransactionPreview;

    @BeforeEach
    void setUp() {
        createTransactionPreview = new CreateTransactionPreview(
                prepareTransaction,
                pendingTransactionStore
        );
    }

    @Test
    void shouldCreateAndStorePreviewSuccessfully() {
        String description = "Monthly salary";
        BigDecimal amount = new BigDecimal("3500.00");
        UUID categoryId = UUID.randomUUID();
        LocalDate transactionDate = LocalDate.of(2026, 11, 12);

        TransactionPreview transaction = new TransactionPreview(
                description,
                amount,
                categoryId,
                "Salary",
                "INCOME",
                transactionDate
        );

        when(prepareTransaction.execute(
                description,
                amount,
                categoryId,
                transactionDate
        )).thenReturn(transaction);

        PendingTransactionPreview result =
                createTransactionPreview.execute(
                        description,
                        amount,
                        categoryId,
                        transactionDate
                );

        assertThat(result.id()).isNotNull();
        assertThat(result.transaction()).isEqualTo(transaction);
        assertThat(result.expiresAt()).isNotNull();

        assertThat(result.expiresAt())
                .isAfter(Instant.now());

        assertThat(result.expiresAt())
                .isBeforeOrEqualTo(Instant.now().plusSeconds(600));

        verify(prepareTransaction).execute(
                description,
                amount,
                categoryId,
                transactionDate
        );

        verify(pendingTransactionStore).save(result);
    }

    @Test
    void shouldGenerateUniquePreviewIds() {
        TransactionPreview transaction = new TransactionPreview(
                "Monthly salary",
                new BigDecimal("3500.00"),
                UUID.randomUUID(),
                "Salary",
                "INCOME",
                LocalDate.of(2026, 11, 12)
        );

        when(prepareTransaction.execute(
                any(),
                any(),
                any(),
                any()
        )).thenReturn(transaction);

        PendingTransactionPreview first =
                createTransactionPreview.execute(
                        "Monthly salary",
                        new BigDecimal("3500.00"),
                        transaction.categoryId(),
                        transaction.transactionDate()
                );

        PendingTransactionPreview second =
                createTransactionPreview.execute(
                        "Monthly salary",
                        new BigDecimal("3500.00"),
                        transaction.categoryId(),
                        transaction.transactionDate()
                );

        assertThat(first.id()).isNotEqualTo(second.id());

        verify(pendingTransactionStore).save(first);
        verify(pendingTransactionStore).save(second);
    }

    @Test
    void shouldNotStorePreviewWhenPreparationFails() {
        String description = "Monthly salary";
        BigDecimal amount = new BigDecimal("3500.00");
        UUID categoryId = UUID.randomUUID();
        LocalDate transactionDate = LocalDate.of(2026, 11, 12);

        when(prepareTransaction.execute(
                description,
                amount,
                categoryId,
                transactionDate
        )).thenThrow(new IllegalArgumentException("Invalid transaction"));

        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                        createTransactionPreview.execute(
                                description,
                                amount,
                                categoryId,
                                transactionDate
                        )
                ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid transaction");

        verify(prepareTransaction).execute(
                description,
                amount,
                categoryId,
                transactionDate
        );

        verify(pendingTransactionStore, never()).save(any());
    }

}