package com.financialassistant.ai_financial_assistant.presentation.dto.response;

import com.financialassistant.ai_financial_assistant.application.dto.PendingTransactionPreview;
import com.financialassistant.ai_financial_assistant.application.dto.TransactionPreview;
import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionPreviewResponse(
        UUID previewId,
        String description,
        BigDecimal amount,
        UUID categoryId,
        String categoryName,
        TransactionType type,
        LocalDate transactionDate,
        Instant expiresAt
) {

    public static TransactionPreviewResponse from(PendingTransactionPreview preview) {
        TransactionPreview transaction = preview.transaction();

        return new TransactionPreviewResponse(
                preview.id(),
                transaction.description(),
                transaction.amount(),
                transaction.categoryId(),
                transaction.categoryName(),
                TransactionType.valueOf(transaction.type()),
                transaction.transactionDate(),
                preview.expiresAt()
        );
    }
}
