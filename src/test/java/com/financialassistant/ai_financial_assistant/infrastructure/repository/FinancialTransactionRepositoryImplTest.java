package com.financialassistant.ai_financial_assistant.infrastructure.repository;

import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.repository.CategoryRepository;
import com.financialassistant.ai_financial_assistant.domain.repository.FinancialTransactionRepository;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class FinancialTransactionRepositoryImplTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FinancialTransactionRepository transactionRepository;

    private Category createCategory() {
        Category category = new Category(
                "Food",
                TransactionType.EXPENSE,
                false
        );

        return categoryRepository.save(category);
    }

    private FinancialTransaction createTransaction(Category category) {
        return new FinancialTransaction(
                "Supermarket",
                new Money(new BigDecimal("150.00")),
                category,
                LocalDate.of(2026, 11, 10)
        );
    }

    @Test
    void shouldSaveAndFindTransactionById() {

        Category category = createCategory();
        FinancialTransaction transaction = createTransaction(category);

        transactionRepository.save(transaction);

        Optional<FinancialTransaction> result =
                transactionRepository.findById(transaction.getId());

        assertThat(result).isPresent();

        FinancialTransaction saved = result.orElseThrow();

        assertThat(saved.getDescription()).isEqualTo("Supermarket");
        assertThat(saved.getAmount().getAmount())
                .isEqualByComparingTo("150.00");
        assertThat(saved.getCategory().getId())
                .isEqualTo(category.getId());
        assertThat(saved.getTransactionDate())
                .isEqualTo(LocalDate.of(2026, 11, 10));
    }

    @Test
    void shouldFindAllTransactions() {

        Category category = createCategory();
        FinancialTransaction transaction = createTransaction(category);

        transactionRepository.save(transaction);

        List<FinancialTransaction> result =
                transactionRepository.findAll();

        assertThat(result)
                .extracting(FinancialTransaction::getId)
                .contains(transaction.getId());
    }

    @Test
    void shouldFindOnlyActiveTransactions() {

        Category category = createCategory();

        FinancialTransaction activeTransaction =
                createTransaction(category);

        FinancialTransaction inactiveTransaction =
                createTransaction(category);

        inactiveTransaction.deactivate();

        transactionRepository.save(activeTransaction);
        transactionRepository.save(inactiveTransaction);

        List<FinancialTransaction> result =
                transactionRepository.findAllActive();

        assertThat(result)
                .extracting(FinancialTransaction::getId)
                .contains(activeTransaction.getId())
                .doesNotContain(inactiveTransaction.getId());
    }

}