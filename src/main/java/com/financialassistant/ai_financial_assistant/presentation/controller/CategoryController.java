package com.financialassistant.ai_financial_assistant.presentation.controller;

import com.financialassistant.ai_financial_assistant.application.usecase.category.CreateCategory;
import com.financialassistant.ai_financial_assistant.application.usecase.category.GetActiveCategories;
import com.financialassistant.ai_financial_assistant.application.usecase.category.GetCategories;
import com.financialassistant.ai_financial_assistant.presentation.dto.request.CreateCategoryRequest;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CreateCategory createCategory;
    private final GetCategories getCategories;
    private final GetActiveCategories getActiveCategories;

    public CategoryController(
            CreateCategory createCategory,
            GetCategories getCategories,
            GetActiveCategories getActiveCategories
    ) {
        this.createCategory = createCategory;
        this.getCategories = getCategories;
        this.getActiveCategories = getActiveCategories;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @Valid @RequestBody CreateCategoryRequest request
    ) {
        var category = createCategory.execute(
                request.name(),
                request.type(),
                request.isDefault()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CategoryResponse.from(category));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        var categories = getCategories.execute()
                .stream()
                .map(CategoryResponse::from)
                .toList();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponse>> findAllActive() {
        var categories = getActiveCategories.execute()
                .stream()
                .map(CategoryResponse::from)
                .toList();

        return ResponseEntity.ok(categories);
    }
}
