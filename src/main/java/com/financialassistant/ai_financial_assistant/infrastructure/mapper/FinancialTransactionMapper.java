package com.financialassistant.ai_financial_assistant.infrastructure.mapper;

import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;
import com.financialassistant.ai_financial_assistant.infrastructure.persistence.entity.CategoryEntity;
import com.financialassistant.ai_financial_assistant.infrastructure.persistence.entity.FinancialTransactionEntity;
import com.financialassistant.ai_financial_assistant.infrastructure.persistence.entity.MoneyEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class FinancialTransactionMapper {

    private final CategoryMapper categoryMapper;

    public FinancialTransactionMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public FinancialTransactionEntity toEntity(FinancialTransaction transaction) {

        CategoryEntity categoryEntity =
                categoryMapper.toEntity(transaction.getCategory());

        MoneyEmbeddable amount =
                new MoneyEmbeddable(transaction.getAmount().getAmount());

        return new FinancialTransactionEntity(
                transaction.getId(),
                transaction.getDescription(),
                amount,
                categoryEntity,
                transaction.getTransactionDate(),
                transaction.isActive(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }

    public FinancialTransaction toDomain(FinancialTransactionEntity entity) {

        Category category = categoryMapper.toDomain(entity.getCategory());

        Money amount = new Money(entity.getAmount().getAmount());

        return FinancialTransaction.reconstitute(
                entity.getId(),
                entity.getDescription(),
                amount,
                category,
                entity.getTransactionDate(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
