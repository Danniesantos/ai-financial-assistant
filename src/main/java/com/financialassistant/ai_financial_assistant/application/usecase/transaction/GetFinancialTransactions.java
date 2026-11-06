package com.financialassistant.ai_financial_assistant.application.usecase.transaction;

import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.repository.FinancialTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetFinancialTransactions {

    private final FinancialTransactionRepository transactionRepository;

    public GetFinancialTransactions(
            FinancialTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<FinancialTransaction> execute() {
        return transactionRepository.findAll();
    }
}
