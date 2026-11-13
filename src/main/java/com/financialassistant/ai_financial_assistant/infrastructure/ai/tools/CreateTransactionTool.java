package com.financialassistant.ai_financial_assistant.infrastructure.ai.tools;

import com.financialassistant.ai_financial_assistant.application.usecase.transaction.CreateFinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class CreateTransactionTool {

    private final CreateFinancialTransaction createFinancialTransaction;

    public CreateTransactionTool(
            CreateFinancialTransaction createFinancialTransaction
    ) {
        this.createFinancialTransaction = createFinancialTransaction;
    }

    @Tool(description = """
            Creates a financial transaction for the user.
            Use this tool only after the user has explicitly confirmed
            the transaction details.
            The category ID must be an existing category UUID.
            The transaction date must use ISO format: yyyy-MM-dd.
            """)
    public TransactionCreationResult createTransaction(
            @ToolParam(description = "Transaction description")
            String description,

            @ToolParam(description = "Transaction amount, greater than zero")
            BigDecimal amount,

            @ToolParam(description = "Existing category UUID")
            String categoryId,

            @ToolParam(description = "Transaction date in yyyy-MM-dd format")
            String transactionDate
    ) {
        FinancialTransaction transaction =
                createFinancialTransaction.execute(
                        description,
                        amount,
                        UUID.fromString(categoryId),
                        LocalDate.parse(transactionDate)
                );

        return new TransactionCreationResult(
                transaction.getId().toString(),
                transaction.getDescription(),
                transaction.getAmount().getAmount(),
                transaction.getCategory().getName(),
                transaction.getType().name(),
                transaction.getTransactionDate(),
                transaction.isActive()
        );
    }

    public record TransactionCreationResult(
            String id,
            String description,
            BigDecimal amount,
            String category,
            String type,
            LocalDate transactionDate,
            boolean active
    ) {
    }
}
