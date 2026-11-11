package com.financialassistant.ai_financial_assistant.presentation.controller;

import com.financialassistant.ai_financial_assistant.application.dto.FinancialSummary;
import com.financialassistant.ai_financial_assistant.application.exception.InvalidDateRangeException;
import com.financialassistant.ai_financial_assistant.application.usecase.transaction.*;
import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinancialTransactionController.class)
class FinancialTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateFinancialTransaction createFinancialTransaction;

    @MockitoBean
    private GetFinancialTransactions getFinancialTransactions;

    @MockitoBean
    private GetFinancialTransactionById getFinancialTransactionById;

    @MockitoBean
    private GetActiveFinancialTransactions getActiveFinancialTransactions;

    @MockitoBean
    private DeactivateFinancialTransaction deactivateFinancialTransaction;

    @MockitoBean
    private ActivateFinancialTransaction activateFinancialTransaction;

    @MockitoBean
    private GetFinancialSummary getFinancialSummary;

    private Category createCategory() {
        return new Category("Food", TransactionType.EXPENSE, false);
    }

    private FinancialTransaction createTransaction() {
        return new FinancialTransaction(
                "Lunch",
                new Money(new BigDecimal("35.50")),
                createCategory(),
                LocalDate.of(2026, 11, 10)
        );
    }

    @Test
    void shouldCreateTransaction() throws Exception {
        FinancialTransaction transaction = createTransaction();

        when(createFinancialTransaction.execute(
                eq("Lunch"),
                eq(new BigDecimal("35.50")),
                any(UUID.class),
                eq(LocalDate.of(2026, 11, 10))
        )).thenReturn(transaction);

        UUID categoryId = UUID.randomUUID();

        String request = """
                {
                    "description": "Lunch",
                    "amount": 35.50,
                    "categoryId": "%s",
                    "transactionDate": "2026-11-10"
                }
                """.formatted(categoryId);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Lunch"))
                .andExpect(jsonPath("$.amount").value(35.50))
                .andExpect(jsonPath("$.categoryName").value("Food"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldReturnAllTransactions() throws Exception {
        FinancialTransaction transaction = createTransaction();

        when(getFinancialTransactions.execute())
                .thenReturn(List.of(transaction));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value("Lunch"));
    }

    @Test
    void shouldReturnActiveTransactions() throws Exception {
        FinancialTransaction transaction = createTransaction();

        when(getActiveFinancialTransactions.execute())
                .thenReturn(List.of(transaction));

        mockMvc.perform(get("/api/transactions/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void shouldReturnTransactionById() throws Exception {
        FinancialTransaction transaction = createTransaction();
        UUID transactionId = transaction.getId();

        when(getFinancialTransactionById.execute(transactionId))
                .thenReturn(transaction);

        mockMvc.perform(get("/api/transactions/{transactionId}", transactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Lunch"));
    }

    @Test
    void shouldDeactivateTransaction() throws Exception {
        FinancialTransaction transaction = createTransaction();
        UUID transactionId = transaction.getId();

        when(deactivateFinancialTransaction.execute(transactionId))
                .thenReturn(transaction);

        mockMvc.perform(patch(
                        "/api/transactions/{transactionId}/deactivate",
                        transactionId
                ))
                .andExpect(status().isOk());
    }

    @Test
    void shouldActivateTransaction() throws Exception {
        FinancialTransaction transaction = createTransaction();
        UUID transactionId = transaction.getId();

        when(activateFinancialTransaction.execute(transactionId))
                .thenReturn(transaction);

        mockMvc.perform(patch(
                        "/api/transactions/{transactionId}/activate",
                        transactionId
                ))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenDescriptionIsBlank() throws Exception {
        String requestBody = """
                {
                    "description": "",
                    "amount": 35.50,
                    "categoryId": "00000000-0000-0000-0000-000000000001",
                    "transactionDate": "2026-11-10"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Request validation failed"))
                .andExpect(jsonPath("$.instance").value("/api/transactions"))
                .andExpect(jsonPath("$.errors.description").value("Description is required"));
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyIsInvalidJson() throws Exception {
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid json }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid request body"))
                .andExpect(jsonPath("$.instance").value("/api/transactions"));
    }

    @Test
    void shouldReturnBadRequestWhenAmountIsZero() throws Exception {
        String requestBody = """
                {
                    "description": "Lunch",
                    "amount": 0,
                    "categoryId": "00000000-0000-0000-0000-000000000001",
                    "transactionDate": "2026-11-10"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.amount").value("Amount must be greater than zero"));
    }

    @Test
    void shouldReturnFinancialSummary() throws Exception {
        LocalDate startDate = LocalDate.of(2026, 11, 1);
        LocalDate endDate = LocalDate.of(2026, 11, 30);

        FinancialSummary summary = new FinancialSummary(
                new BigDecimal("3500.00"),
                new BigDecimal("1150.00"),
                new BigDecimal("2350.00"),
                4
        );

        when(getFinancialSummary.execute(startDate, endDate))
                .thenReturn(summary);

        mockMvc.perform(get("/api/transactions/summary")
                        .param("startDate", "2026-11-01")
                        .param("endDate", "2026-11-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(3500.00))
                .andExpect(jsonPath("$.totalExpenses").value(1150.00))
                .andExpect(jsonPath("$.balance").value(2350.00))
                .andExpect(jsonPath("$.transactionCount").value(4));
    }

    @Test
    void shouldReturnBadRequestWhenSummaryDateRangeIsInvalid() throws Exception {
        LocalDate startDate = LocalDate.of(2026, 11, 30);
        LocalDate endDate = LocalDate.of(2026, 11, 1);

        when(getFinancialSummary.execute(startDate, endDate))
                .thenThrow(new InvalidDateRangeException(
                        "Start date must be before or equal to end date"
                ));

        mockMvc.perform(get("/api/transactions/summary")
                        .param("startDate", "2026-11-30")
                        .param("endDate", "2026-11-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
