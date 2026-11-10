package com.financialassistant.ai_financial_assistant.infrastructure.repository;

import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class CategoryRepositoryImplTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldSaveAndFindCategoryById() {

        Category category = new Category(
                "Food",
                TransactionType.EXPENSE,
                false
        );

        categoryRepository.save(category);

        Optional<Category> result =
                categoryRepository.findById(category.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Food");
        assertThat(result.get().getType())
                .isEqualTo(TransactionType.EXPENSE);
    }

    @Test
    void shouldFindAllCategories() {

        Category category = new Category(
                "Salary",
                TransactionType.INCOME,
                false
        );

        categoryRepository.save(category);

        List<Category> result = categoryRepository.findAll();

        assertThat(result)
                .extracting(Category::getId)
                .contains(category.getId());
    }

    @Test
    void shouldFindOnlyActiveCategories() {
        Category activeCategory = new Category(
                "Food",
                TransactionType.EXPENSE,
                false
        );

        Category inactiveCategory = new Category(
                "Old category",
                TransactionType.EXPENSE,
                false
        );

        inactiveCategory.deactivate();

        categoryRepository.save(activeCategory);
        categoryRepository.save(inactiveCategory);

        List<Category> result = categoryRepository.findAllActive();

        assertThat(result)
                .extracting(Category::getId)
                .contains(activeCategory.getId())
                .doesNotContain(inactiveCategory.getId());
    }
}