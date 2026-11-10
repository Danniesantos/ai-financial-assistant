package com.financialassistant.ai_financial_assistant.presentation.controller;

import com.financialassistant.ai_financial_assistant.application.usecase.category.CreateCategory;
import com.financialassistant.ai_financial_assistant.application.usecase.category.GetActiveCategories;
import com.financialassistant.ai_financial_assistant.application.usecase.category.GetCategories;
import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateCategory createCategory;

    @MockitoBean
    private GetCategories getCategories;

    @MockitoBean
    private GetActiveCategories getActiveCategories;

    @Test
    void shouldCreateCategory() throws Exception {
        Category category = new Category(
                "Food",
                TransactionType.EXPENSE,
                false
        );

        when(createCategory.execute(
                "Food",
                TransactionType.EXPENSE,
                false
        )).thenReturn(category);

        String request = """
                {
                    "name": "Food",
                    "type": "EXPENSE",
                    "isDefault": false
                }
                """;

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Food"))
                .andExpect(jsonPath("$.type").value("EXPENSE"))
                .andExpect(jsonPath("$.active").value(true));

        verify(createCategory).execute(
                "Food",
                TransactionType.EXPENSE,
                false
        );
    }

    @Test
    void shouldReturnAllCategories() throws Exception {
        Category category = new Category(
                "Food",
                TransactionType.EXPENSE,
                false
        );

        when(getCategories.execute()).thenReturn(List.of(category));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Food"));

        verify(getCategories).execute();
    }

    @Test
    void shouldReturnActiveCategories() throws Exception {
        Category category = new Category(
                "Salary",
                TransactionType.INCOME,
                false
        );

        when(getActiveCategories.execute()).thenReturn(List.of(category));

        mockMvc.perform(get("/api/categories/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Salary"))
                .andExpect(jsonPath("$[0].active").value(true));

        verify(getActiveCategories).execute();
    }

    @Test
    void shouldReturnBadRequestWhenCategoryNameIsBlank() throws Exception {
        String request = """
                {
                    "name": " ",
                    "type": "EXPENSE",
                    "isDefault": false
                }
                """;

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(createCategory);
    }

}