package com.financialassistant.ai_financial_assistant.application.usecase.transaction;

import com.financialassistant.ai_financial_assistant.application.dto.FinancialSummary;
import com.financialassistant.ai_financial_assistant.application.exception.InvalidDateRangeException;
import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.repository.FinancialTransactionRepository;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetFinancialSummaryTest {


    @Mock
    private FinancialTransactionRepository transactionRepository;

    @InjectMocks
    private GetFinancialSummary getFinancialSummary;

    @Test
    @DisplayName("Should calculate financial summary correctly")
    void shouldCalculateFinancialSummaryCorrectly() {

        LocalDate startDate = LocalDate.of(2026, 11, 1);
        LocalDate endDate = LocalDate.of(2026, 11, 30);

        List<FinancialTransaction> transactions = List.of(
                createTransaction("Salary", "3000.00", TransactionType.INCOME),
                createTransaction("Freelance", "500.00", TransactionType.INCOME),
                createTransaction("Food", "150.00", TransactionType.EXPENSE),
                createTransaction("Rent", "1000.00", TransactionType.EXPENSE)
        );

        when(transactionRepository.findAllActiveBetween(startDate, endDate))
                .thenReturn(transactions);

        FinancialSummary result =
                getFinancialSummary.execute(startDate, endDate);

        assertThat(result.totalIncome()).isEqualByComparingTo("3500.00");
        assertThat(result.totalExpenses()).isEqualByComparingTo("1150.00");
        assertThat(result.balance()).isEqualByComparingTo("2350.00");
        assertThat(result.transactionCount()).isEqualTo(4);

        verify(transactionRepository).findAllActiveBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Should return zero totals when no transactions exist")
    void shouldReturnZeroTotalsWhenNoTransactionsExist() {

        LocalDate startDate = LocalDate.of(2026, 11, 1);
        LocalDate endDate = LocalDate.of(2026, 11, 30);

        when(transactionRepository.findAllActiveBetween(startDate, endDate))
                .thenReturn(List.of());

        FinancialSummary result =
                getFinancialSummary.execute(startDate, endDate);

        assertThat(result.totalIncome()).isEqualByComparingTo("0.00");
        assertThat(result.totalExpenses()).isEqualByComparingTo("0.00");
        assertThat(result.balance()).isEqualByComparingTo("0.00");
        assertThat(result.transactionCount()).isZero();
    }

    @Test
    @DisplayName("Should throw exception when start date is after end date")
    void shouldThrowExceptionWhenStartDateIsAfterEndDate() {

        LocalDate startDate = LocalDate.of(2026, 11, 30);
        LocalDate endDate = LocalDate.of(2026, 11, 1);

        assertThatThrownBy(() ->
                getFinancialSummary.execute(startDate, endDate)
        ).isInstanceOf(InvalidDateRangeException.class);

        verifyNoInteractions(transactionRepository);
    }

    @Test
    @DisplayName("Should throw exception when dates are null")
    void shouldThrowExceptionWhenDatesAreNull() {

        assertThatThrownBy(() ->
                getFinancialSummary.execute(null, null)
        ).isInstanceOf(InvalidDateRangeException.class);

        verifyNoInteractions(transactionRepository);
    }

    private FinancialTransaction createTransaction(
            String description,
            String amount,
            TransactionType type
    ) {
        Category category = new Category(
                type == TransactionType.INCOME ? "Salary" : "Food",
                type,
                false
        );

        return new FinancialTransaction(
                description,
                new Money(new BigDecimal(amount)),
                category,
                LocalDate.of(2026, 11, 3)
        );
    }
}