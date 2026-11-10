package com.financialassistant.ai_financial_assistant.infrastructure.repository;


import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.repository.CategoryRepository;
import com.financialassistant.ai_financial_assistant.infrastructure.mapper.CategoryMapper;
import com.financialassistant.ai_financial_assistant.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final SpringDataCategoryRepository springDataRepository;
    private final CategoryMapper mapper;

    public CategoryRepositoryImpl(
            SpringDataCategoryRepository springDataRepository,
            CategoryMapper mapper
    ) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = mapper.toEntity(category);

        CategoryEntity savedEntity = springDataRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return springDataRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return springDataRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findAllActive() {
        return springDataRepository.findAllByActiveTrue()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
