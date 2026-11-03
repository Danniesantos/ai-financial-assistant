package com.financialassistant.ai_financial_assistant.domain.valueobject;

import com.financialassistant.ai_financial_assistant.domain.exception.InvalidMoneyException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {

    private static final int SCALE = 2;

    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidMoneyException("Money amount cannot be null.");
        }

        if (amount.scale() > SCALE) {
            throw new InvalidMoneyException(
                    "Money amount cannot have more than two decimal places."
            );
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidMoneyException(
                    "Money amount must be greater than zero."
            );
        }
        this.amount = amount.setScale(SCALE, RoundingMode.UNNECESSARY);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Money add(Money other) {
        Objects.requireNonNull(other, "Money cannot be null.");
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        Objects.requireNonNull(other, "Money cannot be null.");
        return new Money(this.amount.subtract(other.amount));
    }

    public boolean isGreaterThan(Money other) {
        Objects.requireNonNull(other, "Money cannot be null.");
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        Objects.requireNonNull(other, "Money cannot be null.");
        return this.amount.compareTo(other.amount) < 0;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Money money)) {
            return false;
        }

        return amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return amount.stripTrailingZeros().hashCode();
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }

}
