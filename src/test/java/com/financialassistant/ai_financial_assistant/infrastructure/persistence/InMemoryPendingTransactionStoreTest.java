package com.financialassistant.ai_financial_assistant.infrastructure.persistence;

import com.financialassistant.ai_financial_assistant.application.dto.PendingTransactionPreview;
import com.financialassistant.ai_financial_assistant.application.dto.TransactionPreview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class InMemoryPendingTransactionStoreTest {

    private InMemoryPendingTransactionStore store;

    @BeforeEach
    void setUp() {
        store = new InMemoryPendingTransactionStore();
    }

    @Test
    void shouldSaveAndConsumePreviewSuccessfully() {
        Instant now = Instant.parse("2026-11-12T18:00:00Z");

        PendingTransactionPreview preview = createPreview(
                now.plusSeconds(300)
        );

        store.save(preview);

        assertThat(store.consume(preview.id(), now))
                .contains(preview);

        assertThat(store.consume(preview.id(), now))
                .isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenPreviewDoesNotExist() {
        assertThat(store.consume(UUID.randomUUID(), Instant.now()))
                .isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenPreviewIsExpired() {
        Instant now = Instant.parse("2026-11-12T18:00:00Z");

        PendingTransactionPreview preview = createPreview(
                now.minusSeconds(1)
        );

        store.save(preview);

        assertThat(store.consume(preview.id(), now))
                .isEmpty();

        assertThat(store.consume(preview.id(), now))
                .isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenPreviewExpiresAtCurrentTime() {
        Instant now = Instant.parse("2026-11-12T18:00:00Z");

        PendingTransactionPreview preview = createPreview(now);

        store.save(preview);

        assertThat(store.consume(preview.id(), now))
                .isEmpty();
    }

    private PendingTransactionPreview createPreview(Instant expiresAt) {
        TransactionPreview transaction = new TransactionPreview(
                "Monthly salary",
                new BigDecimal("3500.00"),
                UUID.randomUUID(),
                "Salary",
                "INCOME",
                LocalDate.of(2026, 11, 12)
        );

        return new PendingTransactionPreview(
                UUID.randomUUID(),
                transaction,
                expiresAt
        );
    }
}