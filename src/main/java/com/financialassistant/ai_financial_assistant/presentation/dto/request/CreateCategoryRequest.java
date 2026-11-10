package com.financialassistant.ai_financial_assistant.presentation.dto.request;

import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(

        @NotBlank(message = "Category name is required")
        @Size(max = 100, message = "Category name must have at most 100 characters")
        String name,

        @NotNull(message = "Transaction type is required")
        TransactionType type,

        boolean isDefault
) {
}
