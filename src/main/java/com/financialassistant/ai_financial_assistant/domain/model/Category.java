package com.financialassistant.ai_financial_assistant.domain.model;

import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.exception.InvalidCategoryException;

import java.time.Instant;
import java.util.UUID;

public class Category {

    private final UUID id;
    private final String name;
    private final TransactionType type;
    private final boolean isDefault;
    private boolean active;
    private final Instant createdAt;

    public Category(
            String name,
            TransactionType type,
            boolean isDefault
    ) {
        if (name == null || name.isBlank()) {
            throw new InvalidCategoryException(
                    "Category name cannot be null or empty."
            );
        }

        if (type == null) {
            throw new InvalidCategoryException(
                    "Category type cannot be null."
            );
        }

        this.id = UUID.randomUUID();
        this.name = name.trim();
        this.type = type;
        this.isDefault = isDefault;
        this.active = true;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TransactionType getType() {
        return type;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public void validateForNewTransaction() {
        if (!active) {
            throw new InvalidCategoryException(
                    "Inactive categories cannot be used in new transactions."
            );
        }
    }
}
