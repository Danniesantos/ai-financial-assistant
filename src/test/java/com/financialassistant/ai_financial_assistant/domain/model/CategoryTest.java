package com.financialassistant.ai_financial_assistant.domain.model;

import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.exception.InvalidCategoryException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void shouldCreateCategoryWithValidData() {
        Category category = new Category(
                "Food",
                TransactionType.EXPENSE,
                true
        );

        assertNotNull(category.getId());
        assertEquals("Food", category.getName());
        assertEquals(TransactionType.EXPENSE, category.getType());
        assertTrue(category.isDefault());
        assertTrue(category.isActive());
        assertNotNull(category.getCreatedAt());
    }

    @Test
    void shouldGenerateUniqueIdsForDifferentCategories() {
        Category first = new Category(
                "Food", TransactionType.EXPENSE, true
        );

        Category second = new Category(
                "Food", TransactionType.EXPENSE, true
        );

        assertNotEquals(first.getId(), second.getId());
    }

    @Test
    void shouldRejectNullName() {
        assertThrows(
                InvalidCategoryException.class,
                () -> new Category(null, TransactionType.EXPENSE, false)
        );
    }

    @Test
    void shouldRejectEmptyName() {
        assertThrows(
                InvalidCategoryException.class,
                () -> new Category("", TransactionType.EXPENSE, false)
        );
    }

    @Test
    void shouldRejectBlankName() {
        assertThrows(
                InvalidCategoryException.class,
                () -> new Category("   ", TransactionType.EXPENSE, false)
        );
    }

    @Test
    void shouldTrimCategoryName() {
        Category category = new Category(
                "  Food  ",
                TransactionType.EXPENSE,
                false
        );

        assertEquals("Food", category.getName());
    }

    @Test
    void shouldRejectNullType() {
        assertThrows(
                InvalidCategoryException.class,
                () -> new Category("Food", null, false)
        );
    }

    @Test
    void shouldCreateCategoryAsActive() {
        Category category = new Category(
                "Food", TransactionType.EXPENSE, false
        );

        assertTrue(category.isActive());
    }

    @Test
    void shouldDeactivateCategory() {
        Category category = new Category(
                "Food", TransactionType.EXPENSE, false
        );

        category.deactivate();

        assertFalse(category.isActive());
    }

    @Test
    void shouldActivateDeactivatedCategory() {
        Category category = new Category(
                "Food", TransactionType.EXPENSE, false
        );

        category.deactivate();
        category.activate();

        assertTrue(category.isActive());
    }

    @Test
    void shouldPreserveCategoryIdentityAfterStateChanges() {
        Category category = new Category(
                "Food", TransactionType.EXPENSE, false
        );

        UUID originalId = category.getId();
        Instant originalDate = category.getCreatedAt();

        category.deactivate();
        category.activate();

        assertEquals(originalId, category.getId());
        assertEquals(originalDate, category.getCreatedAt());
    }

    @Test
    void shouldAllowActiveCategoryForNewTransaction() {
        Category category = new Category(
                "Food", TransactionType.EXPENSE, false
        );

        assertDoesNotThrow(category::validateForNewTransaction);
    }

    @Test
    void shouldRejectInactiveCategoryForNewTransaction() {
        Category category = new Category(
                "Food", TransactionType.EXPENSE, false
        );

        category.deactivate();

        assertThrows(
                InvalidCategoryException.class,
                category::validateForNewTransaction
        );
    }

    @Test
    void shouldCreateCustomCategory() {
        Category category = new Category(
                "Pet Care", TransactionType.EXPENSE, false
        );

        assertFalse(category.isDefault());
    }
}