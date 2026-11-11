package com.financialassistant.ai_financial_assistant.presentation.dto.response;

import com.financialassistant.ai_financial_assistant.application.dto.FinancialSummary;

import java.math.BigDecimal;

public record FinancialSummaryResponse(
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal balance,
        long transactionCount
) {

    public static FinancialSummaryResponse from(FinancialSummary summary) {
        return new FinancialSummaryResponse(
                summary.totalIncome(),
                summary.totalExpenses(),
                summary.balance(),
                summary.transactionCount()
        );
    }
}
