package com.financialassistant.ai_financial_assistant.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ConfirmTransactionRequest(
        @NotNull UUID previewId
) {
}
