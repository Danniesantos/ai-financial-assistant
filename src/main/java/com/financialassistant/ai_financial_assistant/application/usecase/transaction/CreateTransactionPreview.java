package com.financialassistant.ai_financial_assistant.application.usecase.transaction;

import com.financialassistant.ai_financial_assistant.application.dto.PendingTransactionPreview;
import com.financialassistant.ai_financial_assistant.application.dto.TransactionPreview;
import com.financialassistant.ai_financial_assistant.application.port.PendingTransactionStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class CreateTransactionPreview {

    private static final long PREVIEW_VALIDITY_SECONDS = 600;

    private final PrepareTransaction prepareTransaction;
    private final PendingTransactionStore pendingTransactionStore;

    public CreateTransactionPreview(
            PrepareTransaction prepareTransaction,
            PendingTransactionStore pendingTransactionStore
    ) {
        this.prepareTransaction = prepareTransaction;
        this.pendingTransactionStore = pendingTransactionStore;
    }

    public PendingTransactionPreview execute(
            String description,
            BigDecimal amount,
            UUID categoryId,
            LocalDate transactionDate
    ) {
        TransactionPreview transaction = prepareTransaction.execute(
                description,
                amount,
                categoryId,
                transactionDate
        );

        PendingTransactionPreview preview = new PendingTransactionPreview(
                UUID.randomUUID(),
                transaction,
                Instant.now().plusSeconds(PREVIEW_VALIDITY_SECONDS)
        );

        pendingTransactionStore.save(preview);

        return preview;
    }
}
