package com.financialassistant.ai_financial_assistant.infrastructure.ai.tools;

import com.financialassistant.ai_financial_assistant.application.dto.FinancialSummary;
import com.financialassistant.ai_financial_assistant.application.usecase.transaction.GetFinancialSummary;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FinancialSummaryTool {

    private final GetFinancialSummary getFinancialSummary;

    public FinancialSummaryTool(GetFinancialSummary getFinancialSummary) {
        this.getFinancialSummary = getFinancialSummary;
    }

    @Tool(description = """
            Retrieves the user's financial summary for a given date range.
            Returns total income, total expenses, balance and transaction count.
            Use this tool whenever the user asks about their financial results
            for a specific period. Dates must use ISO format: yyyy-MM-dd.
            """)
    public FinancialSummary getFinancialSummary(
            @ToolParam(description = "Start date of the period, in yyyy-MM-dd format")
            String startDate,

            @ToolParam(description = "End date of the period, in yyyy-MM-dd format")
            String endDate
    ) {
        return getFinancialSummary.execute(
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
    }
}
