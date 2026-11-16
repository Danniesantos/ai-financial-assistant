package com.financialassistant.ai_financial_assistant.application.dto;

import java.time.Instant;
import java.util.UUID;

public record PendingTransactionPreview(
        UUID id,
        TransactionPreview transaction,
        Instant expiresAt
) {
}
