package com.financialassistant.ai_financial_assistant.application.usecase.transaction;

import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.repository.FinancialTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetActiveFinancialTransactions {

    private final FinancialTransactionRepository transactionRepository;

    public GetActiveFinancialTransactions(
            FinancialTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<FinancialTransaction> execute() {
        return transactionRepository.findAllActive();
    }
}
