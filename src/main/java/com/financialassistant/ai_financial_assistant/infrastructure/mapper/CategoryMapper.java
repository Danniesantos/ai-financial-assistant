package com.financialassistant.ai_financial_assistant.infrastructure.mapper;

import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryEntity toEntity(Category category) {
        return new CategoryEntity(
                category.getId(),
                category.getName(),
                category.getType(),
                category.isDefault(),
                category.isActive(),
                category.getCreatedAt()
        );
    }

    public Category toDomain(CategoryEntity entity) {
        return Category.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.isDefault(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }
}
