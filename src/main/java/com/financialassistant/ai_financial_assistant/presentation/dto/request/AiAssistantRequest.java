package com.financialassistant.ai_financial_assistant.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AiAssistantRequest(
        @NotBlank(message = "Prompt is required")
        String prompt
) {
}
