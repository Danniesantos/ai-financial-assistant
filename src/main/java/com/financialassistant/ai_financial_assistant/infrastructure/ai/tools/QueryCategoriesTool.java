package com.financialassistant.ai_financial_assistant.infrastructure.ai.tools;

import com.financialassistant.ai_financial_assistant.application.usecase.category.GetActiveCategories;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryCategoriesTool {

    private final GetActiveCategories getActiveCategories;

    public QueryCategoriesTool(GetActiveCategories getActiveCategories) {
        this.getActiveCategories = getActiveCategories;
    }

    @Tool(description = """
            Retrieves all active categories registered by the user.
            Returns category ID, name, transaction type and default status.
            Use this tool when the user asks about available categories
            or when identifying a category for a financial transaction.
            """)
    public List<CategoryData> getCategories() {
        return getActiveCategories.execute()
                .stream()
                .map(category -> new CategoryData(
                        category.getId().toString(),
                        category.getName(),
                        category.getType().name(),
                        category.isDefault()
                ))
                .toList();
    }

    public record CategoryData(
            String id,
            String name,
            String type,
            boolean isDefault
    ) {
    }
}
