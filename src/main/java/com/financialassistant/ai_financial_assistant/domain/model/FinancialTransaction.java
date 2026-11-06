package com.financialassistant.ai_financial_assistant.domain.model;

import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.exception.InvalidFinancialTransactionException;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class FinancialTransaction {

    private final UUID id;
    private final String description;
    private final Money amount;
    private final Category category;
    private final LocalDate transactionDate;
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;

    public FinancialTransaction(
            String description,
            Money amount,
            Category category,
            LocalDate transactionDate
    ) {
        if (description == null || description.isBlank()) {
            throw new InvalidFinancialTransactionException(
                    "Transaction description cannot be null or empty."
            );
        }

        if (amount == null) {
            throw new InvalidFinancialTransactionException(
                    "Transaction amount cannot be null."
            );
        }

        if (category == null) {
            throw new InvalidFinancialTransactionException(
                    "Transaction category cannot be null."
            );
        }

        if (transactionDate == null) {
            throw new InvalidFinancialTransactionException(
                    "Transaction date cannot be null."
            );
        }

        category.validateForNewTransaction();

        this.id = UUID.randomUUID();
        this.description = description.trim();
        this.amount = amount;
        this.category = category;
        this.transactionDate = transactionDate;
        this.active = true;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    private FinancialTransaction(
            UUID id,
            String description,
            Money amount,
            Category category,
            LocalDate transactionDate,
            boolean active,
            Instant createdAt,
            Instant updatedAt
    ) {
        if (id == null || description == null || description.isBlank()
                || amount == null || category == null || transactionDate == null
                || createdAt == null || updatedAt == null) {
            throw new InvalidFinancialTransactionException(
                    "Invalid financial transaction data."
            );
        }

        this.id = id;
        this.description = description.trim();
        this.amount = amount;
        this.category = category;
        this.transactionDate = transactionDate;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static FinancialTransaction reconstitute(
            UUID id,
            String description,
            Money amount,
            Category category,
            LocalDate transactionDate,
            boolean active,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new FinancialTransaction(
                id,
                description,
                amount,
                category,
                transactionDate,
                active,
                createdAt,
                updatedAt
        );
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Money getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }

    public TransactionType getType() {
        return category.getType();
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void deactivate() {
        if (this.active) {
            this.active = false;
            this.updatedAt = Instant.now();
        }
    }

    public void activate() {
        if (!this.active) {
            this.active = true;
            this.updatedAt = Instant.now();
        }
    }
}
