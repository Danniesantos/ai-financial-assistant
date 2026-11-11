package com.financialassistant.ai_financial_assistant.application.usecase.transaction;

import com.financialassistant.ai_financial_assistant.application.dto.FinancialSummary;
import com.financialassistant.ai_financial_assistant.application.exception.InvalidDateRangeException;
import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.repository.FinancialTransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class GetFinancialSummary {

    private final FinancialTransactionRepository transactionRepository;

    public GetFinancialSummary(
            FinancialTransactionRepository transactionRepository
    ) {
        this.transactionRepository = transactionRepository;
    }

    public FinancialSummary execute(
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException(
                    "Start date must be before or equal to end date"
            );
        }

        List<FinancialTransaction> transactions =
                transactionRepository.findAllActiveBetween(startDate, endDate);

        BigDecimal totalIncome = transactions.stream()
                .filter(transaction ->
                        transaction.getType() == TransactionType.INCOME)
                .map(transaction -> transaction.getAmount().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactions.stream()
                .filter(transaction ->
                        transaction.getType() == TransactionType.EXPENSE)
                .map(transaction -> transaction.getAmount().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpenses);

        return new FinancialSummary(
                totalIncome,
                totalExpenses,
                balance,
                transactions.size()
        );
    }
}
