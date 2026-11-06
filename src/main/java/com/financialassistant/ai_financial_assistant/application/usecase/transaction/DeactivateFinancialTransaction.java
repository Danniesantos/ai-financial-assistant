package com.financialassistant.ai_financial_assistant.application.usecase.transaction;

import com.financialassistant.ai_financial_assistant.application.exception.FinancialTransactionNotFoundException;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.repository.FinancialTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeactivateFinancialTransaction {

    private final FinancialTransactionRepository transactionRepository;

    public DeactivateFinancialTransaction(
            FinancialTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public FinancialTransaction execute(UUID transactionId) {

        FinancialTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new FinancialTransactionNotFoundException(
                        "Financial transaction not found: " + transactionId
                ));

        transaction.deactivate();

        return transactionRepository.save(transaction);
    }
}
