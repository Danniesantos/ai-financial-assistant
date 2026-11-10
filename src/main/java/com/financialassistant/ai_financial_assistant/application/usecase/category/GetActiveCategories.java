package com.financialassistant.ai_financial_assistant.application.usecase.category;

import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetActiveCategories {

    private final CategoryRepository categoryRepository;

    public GetActiveCategories(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> execute() {
        return categoryRepository.findAllActive();
    }
}
