package com.financialassistant.ai_financial_assistant.domain.model;

import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.exception.InvalidCategoryException;
import com.financialassistant.ai_financial_assistant.domain.exception.InvalidFinancialTransactionException;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FinancialTransactionTest {

    private Category createExpenseCategory() {
        return new Category(
                "Food",
                TransactionType.EXPENSE,
                false
        );
    }

    private Category createIncomeCategory() {
        return new Category(
                "Salary",
                TransactionType.INCOME,
                true
        );
    }

    private Money createMoney(String amount) {
        return new Money(new BigDecimal(amount));
    }

    private FinancialTransaction createTransaction() {
        return new FinancialTransaction(
                "Supermarket purchase",
                createMoney("150.00"),
                createExpenseCategory(),
                LocalDate.of(2026, 11, 3)
        );
    }

    @Test
    void shouldCreateFinancialTransactionWithValidData() {
        FinancialTransaction transaction = createTransaction();

        assertNotNull(transaction.getId());
        assertEquals("Supermarket purchase", transaction.getDescription());
        assertEquals(createMoney("150.00"), transaction.getAmount());
        assertEquals(TransactionType.EXPENSE, transaction.getType());
        assertEquals(LocalDate.of(2026, 11, 3), transaction.getTransactionDate());
        assertTrue(transaction.isActive());
        assertNotNull(transaction.getCreatedAt());
        assertNotNull(transaction.getUpdatedAt());
    }

    @Test
    void shouldGenerateUniqueIdsForDifferentTransactions() {
        FinancialTransaction first = createTransaction();
        FinancialTransaction second = createTransaction();

        assertNotEquals(first.getId(), second.getId());
    }

    @Test
    void shouldTrimDescription() {
        FinancialTransaction transaction = new FinancialTransaction(
                "  Grocery shopping  ",
                createMoney("50.00"),
                createExpenseCategory(),
                LocalDate.now()
        );

        assertEquals("Grocery shopping", transaction.getDescription());
    }

    @Test
    void shouldRejectNullDescription() {
        assertThrows(
                InvalidFinancialTransactionException.class,
                () -> new FinancialTransaction(
                        null,
                        createMoney("100.00"),
                        createExpenseCategory(),
                        LocalDate.now()
                )
        );
    }

    @Test
    void shouldRejectBlankDescription() {
        assertThrows(
                InvalidFinancialTransactionException.class,
                () -> new FinancialTransaction(
                        "   ",
                        createMoney("100.00"),
                        createExpenseCategory(),
                        LocalDate.now()
                )
        );
    }

    @Test
    void shouldRejectNullAmount() {
        assertThrows(
                InvalidFinancialTransactionException.class,
                () -> new FinancialTransaction(
                        "Purchase",
                        null,
                        createExpenseCategory(),
                        LocalDate.now()
                )
        );
    }

    @Test
    void shouldRejectNullCategory() {
        assertThrows(
                InvalidFinancialTransactionException.class,
                () -> new FinancialTransaction(
                        "Purchase",
                        createMoney("100.00"),
                        null,
                        LocalDate.now()
                )
        );
    }

    @Test
    void shouldRejectNullTransactionDate() {
        assertThrows(
                InvalidFinancialTransactionException.class,
                () -> new FinancialTransaction(
                        "Purchase",
                        createMoney("100.00"),
                        createExpenseCategory(),
                        null
                )
        );
    }

    @Test
    void shouldRejectInactiveCategory() {
        Category category = createExpenseCategory();
        category.deactivate();

        assertThrows(
                InvalidCategoryException.class,
                () -> new FinancialTransaction(
                        "Purchase",
                        createMoney("100.00"),
                        category,
                        LocalDate.now()
                )
        );
    }

    @Test
    void shouldIdentifyExpenseTransaction() {
        FinancialTransaction transaction = createTransaction();

        assertEquals(TransactionType.EXPENSE, transaction.getType());
    }

    @Test
    void shouldIdentifyIncomeTransaction() {
        FinancialTransaction transaction = new FinancialTransaction(
                "Monthly salary",
                createMoney("3000.00"),
                createIncomeCategory(),
                LocalDate.now()
        );

        assertEquals(TransactionType.INCOME, transaction.getType());
    }

    @Test
    void shouldCreateTransactionAsActive() {
        FinancialTransaction transaction = createTransaction();

        assertTrue(transaction.isActive());
    }

    @Test
    void shouldDeactivateTransaction() {
        FinancialTransaction transaction = createTransaction();

        transaction.deactivate();

        assertFalse(transaction.isActive());
    }

    @Test
    void shouldActivateDeactivatedTransaction() {
        FinancialTransaction transaction = createTransaction();

        transaction.deactivate();
        transaction.activate();

        assertTrue(transaction.isActive());
    }

    @Test
    void shouldPreserveIdentityAfterStateChanges() {
        FinancialTransaction transaction = createTransaction();

        UUID originalId = transaction.getId();

        transaction.deactivate();
        transaction.activate();

        assertEquals(originalId, transaction.getId());
    }

    @Test
    void shouldPreserveCreationDateAfterStateChanges() {
        FinancialTransaction transaction = createTransaction();

        var originalCreatedAt = transaction.getCreatedAt();

        transaction.deactivate();

        assertEquals(originalCreatedAt, transaction.getCreatedAt());
    }

    @Test
    void shouldInitializeUpdatedAtWithCreatedAt() {
        FinancialTransaction transaction = createTransaction();

        assertEquals(
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }

    @Test
    void shouldUpdateUpdatedAtWhenDeactivated() {
        FinancialTransaction transaction = createTransaction();

        var originalUpdatedAt = transaction.getUpdatedAt();

        transaction.deactivate();

        assertFalse(transaction.getUpdatedAt().isBefore(originalUpdatedAt));
    }

    @Test
    void shouldNotChangeUpdatedAtWhenAlreadyDeactivated() {
        FinancialTransaction transaction = createTransaction();

        transaction.deactivate();
        var updatedAt = transaction.getUpdatedAt();

        transaction.deactivate();

        assertEquals(updatedAt, transaction.getUpdatedAt());
    }

    @Test
    void shouldNotChangeUpdatedAtWhenAlreadyActive() {
        FinancialTransaction transaction = createTransaction();

        var updatedAt = transaction.getUpdatedAt();

        transaction.activate();

        assertEquals(updatedAt, transaction.getUpdatedAt());
    }

}