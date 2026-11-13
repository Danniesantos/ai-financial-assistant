package com.financialassistant.ai_financial_assistant.infrastructure.ai.tools;

import com.financialassistant.ai_financial_assistant.application.usecase.transaction.GetFinancialTransactions;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryTransactionsTool {

    private final GetFinancialTransactions getFinancialTransactions;

    public QueryTransactionsTool(
            GetFinancialTransactions getFinancialTransactions
    ) {
        this.getFinancialTransactions = getFinancialTransactions;
    }

    @Tool(description = """
            Retrieves all financial transactions registered by the user.
            Returns transaction ID, description, amount, category, type,
            transaction date and active status.
            Use this tool when the user asks about their registered transactions.
            """)
    public List<TransactionData> getTransactions() {
        return getFinancialTransactions.execute()
                .stream()
                .map(transaction -> new TransactionData(
                        transaction.getId().toString(),
                        transaction.getDescription(),
                        transaction.getAmount().getAmount(),
                        transaction.getCategory().getName(),
                        transaction.getType().name(),
                        transaction.getTransactionDate(),
                        transaction.isActive()
                ))
                .toList();
    }

    public record TransactionData(
            String id,
            String description,
            java.math.BigDecimal amount,
            String category,
            String type,
            java.time.LocalDate transactionDate,
            boolean active
    ) {
    }
}
