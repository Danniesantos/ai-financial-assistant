package com.financialassistant.ai_financial_assistant.application.port;

import com.financialassistant.ai_financial_assistant.application.dto.PendingTransactionPreview;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface PendingTransactionStore {

    void save(PendingTransactionPreview preview);

    Optional<PendingTransactionPreview> consume(
            UUID previewId,
            Instant currentTime
    );
}
