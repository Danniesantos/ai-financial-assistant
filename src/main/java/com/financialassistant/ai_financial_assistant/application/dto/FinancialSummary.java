package com.financialassistant.ai_financial_assistant.application.dto;

import java.math.BigDecimal;

public record FinancialSummary(
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal balance,
        long transactionCount
) {
}
