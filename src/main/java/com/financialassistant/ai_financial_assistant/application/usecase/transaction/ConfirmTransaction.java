package com.financialassistant.ai_financial_assistant.application.usecase.transaction;

import com.financialassistant.ai_financial_assistant.application.dto.PendingTransactionPreview;
import com.financialassistant.ai_financial_assistant.application.dto.TransactionPreview;
import com.financialassistant.ai_financial_assistant.application.exception.PendingTransactionPreviewNotFoundException;
import com.financialassistant.ai_financial_assistant.application.port.PendingTransactionStore;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ConfirmTransaction {

    private final PendingTransactionStore pendingTransactionStore;
    private final CreateFinancialTransaction createFinancialTransaction;

    public ConfirmTransaction(
            PendingTransactionStore pendingTransactionStore,
            CreateFinancialTransaction createFinancialTransaction
    ) {
        this.pendingTransactionStore = pendingTransactionStore;
        this.createFinancialTransaction = createFinancialTransaction;
    }

    public FinancialTransaction execute(UUID previewId) {

        PendingTransactionPreview pendingPreview = pendingTransactionStore
                .consume(previewId, Instant.now())
                .orElseThrow(() ->
                        new PendingTransactionPreviewNotFoundException(
                                "Transaction preview not found or expired: " + previewId
                        )
                );

        TransactionPreview preview = pendingPreview.transaction();

        return createFinancialTransaction.execute(
                preview.description(),
                preview.amount(),
                preview.categoryId(),
                preview.transactionDate()
        );
    }
}
