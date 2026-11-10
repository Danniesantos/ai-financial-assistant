package com.financialassistant.ai_financial_assistant.infrastructure.repository;

import com.financialassistant.ai_financial_assistant.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataCategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    List<CategoryEntity> findAllByActiveTrue();
}
