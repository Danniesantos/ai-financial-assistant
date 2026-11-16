package com.financialassistant.ai_financial_assistant.infrastructure.persistence;

import com.financialassistant.ai_financial_assistant.application.dto.PendingTransactionPreview;
import com.financialassistant.ai_financial_assistant.application.port.PendingTransactionStore;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryPendingTransactionStore implements PendingTransactionStore {

    private final ConcurrentHashMap<UUID, PendingTransactionPreview> previews =
            new ConcurrentHashMap<>();

    @Override
    public void save(PendingTransactionPreview preview) {
        previews.put(preview.id(), preview);
    }

    @Override
    public Optional<PendingTransactionPreview> consume(
            UUID previewId,
            Instant currentTime
    ) {
        PendingTransactionPreview preview = previews.remove(previewId);

        if (preview == null || !preview.expiresAt().isAfter(currentTime)) {
            return Optional.empty();
        }

        return Optional.of(preview);
    }
}
