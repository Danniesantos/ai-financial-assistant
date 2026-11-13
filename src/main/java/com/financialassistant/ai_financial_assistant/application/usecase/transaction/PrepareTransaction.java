package com.financialassistant.ai_financial_assistant.application.usecase.transaction;

import com.financialassistant.ai_financial_assistant.application.dto.TransactionPreview;
import com.financialassistant.ai_financial_assistant.application.exception.CategoryNotFoundException;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.repository.CategoryRepository;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class PrepareTransaction {

    private final CategoryRepository categoryRepository;

    public PrepareTransaction(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public TransactionPreview execute(
            String description,
            BigDecimal amount,
            UUID categoryId,
            LocalDate transactionDate
    ) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found: " + categoryId
                        )
                );

        FinancialTransaction transaction = new FinancialTransaction(
                description,
                new Money(amount),
                category,
                transactionDate
        );

        return new TransactionPreview(
                transaction.getDescription(),
                transaction.getAmount().getAmount(),
                category.getId(),
                category.getName(),
                transaction.getType().name(),
                transaction.getTransactionDate()
        );
    }
}
