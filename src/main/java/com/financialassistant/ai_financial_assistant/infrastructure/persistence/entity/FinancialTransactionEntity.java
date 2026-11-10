package com.financialassistant.ai_financial_assistant.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "financial_transactions")
public class FinancialTransactionEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 255)
    private String description;

    @Embedded
    private MoneyEmbeddable amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected FinancialTransactionEntity() {
    }

    public FinancialTransactionEntity(
            UUID id,
            String description,
            MoneyEmbeddable amount,
            CategoryEntity category,
            LocalDate transactionDate,
            boolean active,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.transactionDate = transactionDate;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public MoneyEmbeddable getAmount() {
        return amount;
    }

    public CategoryEntity getCategory() {
        return category;
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
}
