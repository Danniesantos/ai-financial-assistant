package com.financialassistant.ai_financial_assistant.infrastructure.persistence.entity;

import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected CategoryEntity() {
    }

    public CategoryEntity(
            UUID id,
            String name,
            TransactionType type,
            boolean isDefault,
            boolean active,
            Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isDefault = isDefault;
        this.active = active;
        this.createdAt = createdAt;
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
}
