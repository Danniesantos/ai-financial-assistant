package com.financialassistant.ai_financial_assistant.infrastructure.repository;

import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.repository.FinancialTransactionRepository;
import com.financialassistant.ai_financial_assistant.infrastructure.mapper.FinancialTransactionMapper;
import com.financialassistant.ai_financial_assistant.infrastructure.persistence.entity.FinancialTransactionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FinancialTransactionRepositoryImpl implements FinancialTransactionRepository {

    private final SpringDataFinancialTransactionRepository springDataRepository;
    private final FinancialTransactionMapper mapper;

    public FinancialTransactionRepositoryImpl(
            SpringDataFinancialTransactionRepository springDataRepository,
            FinancialTransactionMapper mapper
    ) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public FinancialTransaction save(FinancialTransaction transaction) {
        FinancialTransactionEntity entity = mapper.toEntity(transaction);

        FinancialTransactionEntity savedEntity =
                springDataRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<FinancialTransaction> findById(UUID id) {
        return springDataRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<FinancialTransaction> findAll() {
        return springDataRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<FinancialTransaction> findAllActive() {
        return springDataRepository.findAllByActiveTrue()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
