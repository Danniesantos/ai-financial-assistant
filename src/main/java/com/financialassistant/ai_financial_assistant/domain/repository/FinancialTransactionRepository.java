package com.financialassistant.ai_financial_assistant.domain.repository;

import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FinancialTransactionRepository {

    FinancialTransaction save(FinancialTransaction transaction);

    Optional<FinancialTransaction> findById(UUID id);

    List<FinancialTransaction> findAll();

    List<FinancialTransaction> findAllActive();

    List<FinancialTransaction> findAllActiveBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}
