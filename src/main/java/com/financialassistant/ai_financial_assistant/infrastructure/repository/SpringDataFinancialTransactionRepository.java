package com.financialassistant.ai_financial_assistant.infrastructure.repository;

import com.financialassistant.ai_financial_assistant.infrastructure.persistence.entity.FinancialTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SpringDataFinancialTransactionRepository extends JpaRepository<FinancialTransactionEntity, UUID> {

    List<FinancialTransactionEntity> findAllByActiveTrue();

    List<FinancialTransactionEntity>
    findAllByActiveTrueAndTransactionDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}
