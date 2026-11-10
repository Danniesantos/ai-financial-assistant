package com.financialassistant.ai_financial_assistant.presentation.dto.response;

import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record FinancialTransactionResponse(
        UUID id,
        String description,
        BigDecimal amount,
        UUID categoryId,
        String categoryName,
        TransactionType type,
        LocalDate transactionDate,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {

    public static FinancialTransactionResponse from(FinancialTransaction transaction) {
        return new FinancialTransactionResponse(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount().getAmount(),
                transaction.getCategory().getId(),
                transaction.getCategory().getName(),
                transaction.getType(),
                transaction.getTransactionDate(),
                transaction.isActive(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }
}
