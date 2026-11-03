package com.financialassistant.ai_financial_assistant.domain.valueobject;

import com.financialassistant.ai_financial_assistant.domain.exception.InvalidMoneyException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoneyWithValidAmount() {
        Money money = new Money(new BigDecimal("100.00"));

        assertEquals(new BigDecimal("100.00"), money.getAmount());
    }

    @Test
    void shouldAcceptAmountWithOneDecimalPlace() {
        Money money = new Money(new BigDecimal("10.5"));

        assertEquals(new BigDecimal("10.50"), money.getAmount());
    }

    @Test
    void shouldRejectNullAmount() {
        assertThrows(
                InvalidMoneyException.class,
                () -> new Money(null)
        );
    }

    @Test
    void shouldRejectZeroAmount() {
        assertThrows(
                InvalidMoneyException.class,
                () -> new Money(BigDecimal.ZERO)
        );
    }

    @Test
    void shouldRejectNegativeAmount() {
        assertThrows(
                InvalidMoneyException.class,
                () -> new Money(new BigDecimal("-10.00"))
        );
    }

    @Test
    void shouldRejectAmountWithMoreThanTwoDecimalPlaces() {
        assertThrows(
                InvalidMoneyException.class,
                () -> new Money(new BigDecimal("10.999"))
        );
    }

    @Test
    void shouldAddTwoMoneyValues() {
        Money first = new Money(new BigDecimal("100.00"));
        Money second = new Money(new BigDecimal("50.00"));

        Money result = first.add(second);

        assertEquals(new BigDecimal("150.00"), result.getAmount());
    }

    @Test
    void shouldNotModifyOriginalValuesWhenAdding() {
        Money first = new Money(new BigDecimal("100.00"));
        Money second = new Money(new BigDecimal("50.00"));

        first.add(second);

        assertEquals(new BigDecimal("100.00"), first.getAmount());
        assertEquals(new BigDecimal("50.00"), second.getAmount());
    }

    @Test
    void shouldRejectNullWhenAdding() {
        Money money = new Money(new BigDecimal("100.00"));

        assertThrows(
                NullPointerException.class,
                () -> money.add(null)
        );
    }

    @Test
    void shouldSubtractTwoMoneyValues() {
        Money first = new Money(new BigDecimal("100.00"));
        Money second = new Money(new BigDecimal("40.00"));

        Money result = first.subtract(second);

        assertEquals(new BigDecimal("60.00"), result.getAmount());
    }

    @Test
    void shouldRejectSubtractionResultEqualToZero() {
        Money first = new Money(new BigDecimal("50.00"));
        Money second = new Money(new BigDecimal("50.00"));

        assertThrows(
                InvalidMoneyException.class,
                () -> first.subtract(second)
        );
    }

    @Test
    void shouldRejectNegativeSubtractionResult() {
        Money first = new Money(new BigDecimal("30.00"));
        Money second = new Money(new BigDecimal("50.00"));

        assertThrows(
                InvalidMoneyException.class,
                () -> first.subtract(second)
        );
    }

    @Test
    void shouldRejectNullWhenSubtracting() {
        Money money = new Money(new BigDecimal("100.00"));

        assertThrows(
                NullPointerException.class,
                () -> money.subtract(null)
        );
    }

    @Test
    void shouldReturnTrueWhenAmountIsGreaterThanOther() {
        Money first = new Money(new BigDecimal("100.00"));
        Money second = new Money(new BigDecimal("50.00"));

        assertTrue(first.isGreaterThan(second));
    }

    @Test
    void shouldReturnFalseWhenAmountIsNotGreaterThanOther() {
        Money first = new Money(new BigDecimal("50.00"));
        Money second = new Money(new BigDecimal("100.00"));

        assertFalse(first.isGreaterThan(second));
    }

    @Test
    void shouldReturnTrueWhenAmountIsLessThanOther() {
        Money first = new Money(new BigDecimal("50.00"));
        Money second = new Money(new BigDecimal("100.00"));

        assertTrue(first.isLessThan(second));
    }

    @Test
    void shouldReturnFalseWhenAmountIsNotLessThanOther() {
        Money first = new Money(new BigDecimal("100.00"));
        Money second = new Money(new BigDecimal("50.00"));

        assertFalse(first.isLessThan(second));
    }

    @Test
    void shouldRejectNullWhenComparing() {
        Money money = new Money(new BigDecimal("100.00"));

        assertThrows(
                NullPointerException.class,
                () -> money.isGreaterThan(null)
        );

        assertThrows(
                NullPointerException.class,
                () -> money.isLessThan(null)
        );
    }

    @Test
    void shouldConsiderMoneyObjectsWithSameAmountEqual() {
        Money first = new Money(new BigDecimal("100.00"));
        Money second = new Money(new BigDecimal("100.00"));

        assertEquals(first, second);
    }

    @Test
    void shouldConsiderDifferentScalesEqual() {
        Money first = new Money(new BigDecimal("100.0"));
        Money second = new Money(new BigDecimal("100.00"));

        assertEquals(first, second);
    }

    @Test
    void shouldReturnSameHashCodeForEqualMoneyObjects() {
        Money first = new Money(new BigDecimal("100.0"));
        Money second = new Money(new BigDecimal("100.00"));

        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void shouldNotConsiderDifferentAmountsEqual() {
        Money first = new Money(new BigDecimal("100.00"));
        Money second = new Money(new BigDecimal("200.00"));

        assertNotEquals(first, second);
    }

    @Test
    void shouldReturnFormattedAmountAsString() {
        Money money = new Money(new BigDecimal("100.50"));

        assertEquals("100.50", money.toString());
    }
}