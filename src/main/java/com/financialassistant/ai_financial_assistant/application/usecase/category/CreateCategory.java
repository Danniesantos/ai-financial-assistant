package com.financialassistant.ai_financial_assistant.application.usecase.category;

import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateCategory {

    private final CategoryRepository categoryRepository;

    public CreateCategory(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category execute(
            String name,
            TransactionType type,
            boolean isDefault
    ) {
        Category category = new Category(
                name,
                type,
                isDefault
        );

        return categoryRepository.save(category);
    }
}
