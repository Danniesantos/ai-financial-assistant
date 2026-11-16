package com.financialassistant.ai_financial_assistant.infrastructure.ai;

import com.financialassistant.ai_financial_assistant.application.dto.FinancialSummary;
import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.repository.CategoryRepository;
import com.financialassistant.ai_financial_assistant.domain.repository.FinancialTransactionRepository;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;
import com.financialassistant.ai_financial_assistant.infrastructure.ai.tools.FinancialSummaryTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
public class FinancialSummaryToolIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private FinancialSummaryTool financialSummaryTool;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FinancialTransactionRepository transactionRepository;

    @Test
    void shouldReturnFinancialSummaryFromPersistedTransactions() {

        Category incomeCategory = categoryRepository.save(
                new Category(
                        "Salary",
                        TransactionType.INCOME,
                        false
                )
        );

        Category expenseCategory = categoryRepository.save(
                new Category(
                        "Food",
                        TransactionType.EXPENSE,
                        false
                )
        );

        FinancialTransaction salary = createTransaction(
                "Salary",
                "3000.00",
                incomeCategory,
                LocalDate.of(2026, 11, 5)
        );

        FinancialTransaction freelance = createTransaction(
                "Freelance",
                "500.00",
                incomeCategory,
                LocalDate.of(2026, 11, 10)
        );

        FinancialTransaction food = createTransaction(
                "Food",
                "150.00",
                expenseCategory,
                LocalDate.of(2026, 11, 12)
        );

        FinancialTransaction rent = createTransaction(
                "Rent",
                "1000.00",
                expenseCategory,
                LocalDate.of(2026, 11, 15)
        );

        FinancialTransaction outsidePeriod = createTransaction(
                "December expense",
                "700.00",
                expenseCategory,
                LocalDate.of(2026, 12, 5)
        );

        FinancialTransaction inactiveTransaction = createTransaction(
                "Inactive expense",
                "999.00",
                expenseCategory,
                LocalDate.of(2026, 11, 20)
        );

        inactiveTransaction.deactivate();

        transactionRepository.save(salary);
        transactionRepository.save(freelance);
        transactionRepository.save(food);
        transactionRepository.save(rent);
        transactionRepository.save(outsidePeriod);
        transactionRepository.save(inactiveTransaction);

        FinancialSummary result =
                financialSummaryTool.getFinancialSummary(
                        "2026-11-01",
                        "2026-11-30"
                );

        assertThat(result.totalIncome())
                .isEqualByComparingTo("3500.00");

        assertThat(result.totalExpenses())
                .isEqualByComparingTo("1150.00");

        assertThat(result.balance())
                .isEqualByComparingTo("2350.00");

        assertThat(result.transactionCount()).isEqualTo(4);
    }

    private FinancialTransaction createTransaction(
            String description,
            String amount,
            Category category,
            LocalDate date
    ) {
        return new FinancialTransaction(
                description,
                new Money(new BigDecimal(amount)),
                category,
                date
        );
    }
}
