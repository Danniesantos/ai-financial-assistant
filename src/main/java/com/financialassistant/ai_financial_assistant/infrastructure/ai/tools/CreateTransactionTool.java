package com.financialassistant.ai_financial_assistant.infrastructure.ai.tools;

import com.financialassistant.ai_financial_assistant.application.dto.PendingTransactionPreview;
import com.financialassistant.ai_financial_assistant.application.dto.TransactionPreview;
import com.financialassistant.ai_financial_assistant.application.usecase.transaction.CreateTransactionPreview;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class CreateTransactionTool {

    private final CreateTransactionPreview createTransactionPreview;

    public CreateTransactionTool(
            CreateTransactionPreview createTransactionPreview
    ) {
        this.createTransactionPreview = createTransactionPreview;
    }

    @Tool(description = """
            Prepares a financial transaction preview for the user.
            This tool does not save the transaction.
            Always present the transaction details and wait for explicit
            user confirmation before the application persists it.
            The category ID must be an existing category UUID.
            The transaction date must use ISO format: yyyy-MM-dd.
            """)
    public TransactionPreviewResult createTransaction(
            @ToolParam(description = "Transaction description")
            String description,

            @ToolParam(description = "Transaction amount, greater than zero")
            BigDecimal amount,

            @ToolParam(description = "Existing category UUID")
            String categoryId,

            @ToolParam(description = "Transaction date in yyyy-MM-dd format")
            String transactionDate
    ) {
        PendingTransactionPreview preview =
                createTransactionPreview.execute(
                        description,
                        amount,
                        UUID.fromString(categoryId),
                        LocalDate.parse(transactionDate)
                );

        TransactionPreview transaction = preview.transaction();

        return new TransactionPreviewResult(
                preview.id().toString(),
                transaction.description(),
                transaction.amount(),
                transaction.categoryName(),
                transaction.type(),
                transaction.transactionDate(),
                preview.expiresAt()
        );
    }

    public record TransactionPreviewResult(
            String previewId,
            String description,
            BigDecimal amount,
            String category,
            String type,
            LocalDate transactionDate,
            Instant expiresAt
    ) {
    }
}
