package com.financialassistant.ai_financial_assistant.presentation.controller;

import com.financialassistant.ai_financial_assistant.application.usecase.category.CreateCategory;
import com.financialassistant.ai_financial_assistant.application.usecase.category.GetActiveCategories;
import com.financialassistant.ai_financial_assistant.application.usecase.category.GetCategories;
import com.financialassistant.ai_financial_assistant.presentation.dto.request.CreateCategoryRequest;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Categories",
        description = "Endpoints for financial category management"
)
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

    @Operation(
            summary = "Create a category",
            description = "Creates a new financial category for income or expenses."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category data")
    })
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

    @Operation(
            summary = "List all categories",
            description = "Returns all registered financial categories, including inactive ones."
    )
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        var categories = getCategories.execute()
                .stream()
                .map(CategoryResponse::from)
                .toList();

        return ResponseEntity.ok(categories);
    }

    @Operation(
            summary = "List active categories",
            description = "Returns only active financial categories."
    )
    @ApiResponse(responseCode = "200", description = "Active categories retrieved successfully")
    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponse>> findAllActive() {
        var categories = getActiveCategories.execute()
                .stream()
                .map(CategoryResponse::from)
                .toList();

        return ResponseEntity.ok(categories);
    }
}
