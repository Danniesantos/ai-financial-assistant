package com.financialassistant.ai_financial_assistant.presentation.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateFinancialTransactionRequest(

        @NotBlank(message = "Description is required")
        @Size(max = 255, message = "Description must have at most 255 characters")
        String description,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Category ID is required")
        UUID categoryId,

        @NotNull(message = "Transaction date is required")
        LocalDate transactionDate
) {
}
