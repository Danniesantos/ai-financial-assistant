package com.financialassistant.ai_financial_assistant.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionPreview(
        String description,
        BigDecimal amount,
        UUID categoryId,
        String categoryName,
        String type,
        LocalDate transactionDate
) {
}
