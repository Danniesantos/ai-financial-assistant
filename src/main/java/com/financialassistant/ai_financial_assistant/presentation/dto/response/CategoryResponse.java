package com.financialassistant.ai_financial_assistant.presentation.dto.response;

import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;

import java.time.Instant;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        TransactionType type,
        boolean isDefault,
        boolean active,
        Instant createdAt
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType(),
                category.isDefault(),
                category.isActive(),
                category.getCreatedAt()
        );
    }
}
