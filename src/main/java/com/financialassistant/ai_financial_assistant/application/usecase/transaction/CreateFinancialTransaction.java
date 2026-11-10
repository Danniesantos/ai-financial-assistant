package com.financialassistant.ai_financial_assistant.application.usecase.transaction;

import com.financialassistant.ai_financial_assistant.application.exception.CategoryNotFoundException;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.repository.CategoryRepository;
import com.financialassistant.ai_financial_assistant.domain.repository.FinancialTransactionRepository;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class CreateFinancialTransaction {

    private final CategoryRepository categoryRepository;
    private final FinancialTransactionRepository transactionRepository;

    public CreateFinancialTransaction(
            CategoryRepository categoryRepository,
            FinancialTransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    public FinancialTransaction execute(
            String description,
            BigDecimal amount,
            UUID categoryId,
            LocalDate transactionDate) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found: " + categoryId
                ));

        Money money = new Money(amount);

        FinancialTransaction transaction = new FinancialTransaction(
                description,
                money,
                category,
                transactionDate
        );

        return transactionRepository.save(transaction);
    }
}
