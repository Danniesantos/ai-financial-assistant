package com.financialassistant.ai_financial_assistant.infrastructure.ai.tools;

import com.financialassistant.ai_financial_assistant.application.usecase.transaction.GetFinancialTransactions;
import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryTransactionsToolTest {

    @Mock
    private GetFinancialTransactions getFinancialTransactions;

    private QueryTransactionsTool queryTransactionsTool;

    @BeforeEach
    void setUp() {
        queryTransactionsTool = new QueryTransactionsTool(
                getFinancialTransactions
        );
    }

    @Test
    void shouldReturnTransactionData() {
        Category category = new Category(
                "Salary",
                TransactionType.INCOME,
                false
        );

        FinancialTransaction transaction = new FinancialTransaction(
                "Monthly salary",
                new Money(new BigDecimal("3500.00")),
                category,
                LocalDate.of(2026, 11, 5)
        );

        when(getFinancialTransactions.execute())
                .thenReturn(List.of(transaction));

        List<QueryTransactionsTool.TransactionData> result =
                queryTransactionsTool.getTransactions();

        assertThat(result).hasSize(1);

        QueryTransactionsTool.TransactionData data = result.get(0);

        assertThat(data.id()).isEqualTo(transaction.getId().toString());
        assertThat(data.description()).isEqualTo("Monthly salary");
        assertThat(data.amount()).isEqualByComparingTo("3500.00");
        assertThat(data.category()).isEqualTo("Salary");
        assertThat(data.type()).isEqualTo("INCOME");
        assertThat(data.transactionDate())
                .isEqualTo(LocalDate.of(2026, 11, 5));
        assertThat(data.active()).isTrue();

        verify(getFinancialTransactions).execute();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoTransactions() {
        when(getFinancialTransactions.execute())
                .thenReturn(List.of());

        List<QueryTransactionsTool.TransactionData> result =
                queryTransactionsTool.getTransactions();

        assertThat(result).isEmpty();

        verify(getFinancialTransactions).execute();
    }

    @Test
    void shouldIncludeInactiveTransactions() {
        Category category = new Category(
                "Food",
                TransactionType.EXPENSE,
                false
        );

        FinancialTransaction transaction = new FinancialTransaction(
                "Inactive food expense",
                new Money(new BigDecimal("150.00")),
                category,
                LocalDate.of(2026, 11, 10)
        );

        transaction.deactivate();

        when(getFinancialTransactions.execute())
                .thenReturn(List.of(transaction));

        List<QueryTransactionsTool.TransactionData> result =
                queryTransactionsTool.getTransactions();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).active()).isFalse();
        assertThat(result.get(0).description())
                .isEqualTo("Inactive food expense");

        verify(getFinancialTransactions).execute();
    }

}