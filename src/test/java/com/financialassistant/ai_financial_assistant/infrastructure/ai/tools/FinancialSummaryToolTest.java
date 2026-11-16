package com.financialassistant.ai_financial_assistant.infrastructure.ai.tools;

import com.financialassistant.ai_financial_assistant.application.dto.FinancialSummary;
import com.financialassistant.ai_financial_assistant.application.exception.InvalidDateRangeException;
import com.financialassistant.ai_financial_assistant.application.usecase.transaction.GetFinancialSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class FinancialSummaryToolTest {

    private GetFinancialSummary getFinancialSummary;
    private FinancialSummaryTool financialSummaryTool;

    @BeforeEach
    void setUp() {
        getFinancialSummary = mock(GetFinancialSummary.class);
        financialSummaryTool = new FinancialSummaryTool(getFinancialSummary);
    }

    @Test
    void shouldReturnFinancialSummaryForDateRange() {
        String startDate = "2026-10-01";
        String endDate = "2026-10-31";

        LocalDate expectedStartDate = LocalDate.of(2026, 10, 1);
        LocalDate expectedEndDate = LocalDate.of(2026, 10, 31);

        FinancialSummary expectedSummary = new FinancialSummary(
                new BigDecimal("5000.00"),
                new BigDecimal("2000.00"),
                new BigDecimal("3000.00"),
                5
        );

        when(getFinancialSummary.execute(expectedStartDate, expectedEndDate))
                .thenReturn(expectedSummary);

        FinancialSummary result = financialSummaryTool.getFinancialSummary(
                startDate,
                endDate
        );

        assertThat(result).isSameAs(expectedSummary);

        verify(getFinancialSummary).execute(
                expectedStartDate,
                expectedEndDate
        );
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsInvalid() {
        assertThatThrownBy(() ->
                financialSummaryTool.getFinancialSummary(
                        "invalid-date",
                        "2026-10-31"
                )
        ).isInstanceOf(java.time.format.DateTimeParseException.class);

        verifyNoInteractions(getFinancialSummary);
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsInvalid() {
        assertThatThrownBy(() ->
                financialSummaryTool.getFinancialSummary(
                        "2026-10-01",
                        "invalid-date"
                )
        ).isInstanceOf(java.time.format.DateTimeParseException.class);

        verifyNoInteractions(getFinancialSummary);
    }

    @Test
    void shouldPropagateExceptionWhenDateRangeIsInvalid() {
        LocalDate startDate = LocalDate.of(2026, 10, 31);
        LocalDate endDate = LocalDate.of(2026, 10, 1);

        when(getFinancialSummary.execute(startDate, endDate))
                .thenThrow(new InvalidDateRangeException(
                        "Start date must be before or equal to end date"
                ));

        assertThatThrownBy(() ->
                financialSummaryTool.getFinancialSummary(
                        "2026-10-31",
                        "2026-10-01"
                )
        ).isInstanceOf(InvalidDateRangeException.class);

        verify(getFinancialSummary).execute(startDate, endDate);
    }

}