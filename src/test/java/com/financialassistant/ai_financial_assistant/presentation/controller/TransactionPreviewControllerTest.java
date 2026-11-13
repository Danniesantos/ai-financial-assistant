package com.financialassistant.ai_financial_assistant.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financialassistant.ai_financial_assistant.application.dto.PendingTransactionPreview;
import com.financialassistant.ai_financial_assistant.application.dto.TransactionPreview;
import com.financialassistant.ai_financial_assistant.application.exception.PendingTransactionPreviewNotFoundException;
import com.financialassistant.ai_financial_assistant.application.usecase.transaction.ConfirmTransaction;
import com.financialassistant.ai_financial_assistant.application.usecase.transaction.CreateTransactionPreview;
import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;
import com.financialassistant.ai_financial_assistant.presentation.dto.request.ConfirmTransactionRequest;
import com.financialassistant.ai_financial_assistant.presentation.dto.request.CreateFinancialTransactionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionPreviewController.class)
class TransactionPreviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();

    @MockitoBean
    private CreateTransactionPreview createTransactionPreview;

    @MockitoBean
    private ConfirmTransaction confirmTransaction;

    @Test
    @DisplayName("Should create transaction preview successfully")
    void shouldCreateTransactionPreviewSuccessfully() throws Exception {
        UUID categoryId = UUID.randomUUID();
        UUID previewId = UUID.randomUUID();

        CreateFinancialTransactionRequest request =
                new CreateFinancialTransactionRequest(
                        "Salary",
                        new BigDecimal("5000.00"),
                        categoryId,
                        LocalDate.of(2026, 11, 12)
                );

        TransactionPreview transactionPreview = new TransactionPreview(
                "Salary",
                new BigDecimal("5000.00"),
                categoryId,
                "Salary",
                "INCOME",
                LocalDate.of(2026, 11, 12)
        );

        PendingTransactionPreview pendingPreview = new PendingTransactionPreview(
                previewId,
                transactionPreview,
                Instant.parse("2026-11-12T19:00:00Z")
        );

        when(createTransactionPreview.execute(
                request.description(),
                request.amount(),
                request.categoryId(),
                request.transactionDate()
        )).thenReturn(pendingPreview);

        mockMvc.perform(post("/api/transactions/preview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.previewId").value(previewId.toString()))
                .andExpect(jsonPath("$.description").value("Salary"))
                .andExpect(jsonPath("$.amount").value(5000.00))
                .andExpect(jsonPath("$.categoryName").value("Salary"))
                .andExpect(jsonPath("$.type").value("INCOME"));

        verify(createTransactionPreview).execute(
                request.description(),
                request.amount(),
                request.categoryId(),
                request.transactionDate()
        );
    }

    @Test
    @DisplayName("Should confirm transaction preview successfully")
    void shouldConfirmTransactionPreviewSuccessfully() throws Exception {
        UUID previewId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ConfirmTransactionRequest request =
                new ConfirmTransactionRequest(previewId);

        FinancialTransaction transaction = mock(FinancialTransaction.class);
        Money money = mock(Money.class);
        Category category = mock(Category.class);

        when(confirmTransaction.execute(previewId)).thenReturn(transaction);

        when(transaction.getId()).thenReturn(transactionId);
        when(transaction.getDescription()).thenReturn("Salary");
        when(transaction.getAmount()).thenReturn(money);
        when(money.getAmount()).thenReturn(new BigDecimal("5000.00"));
        when(transaction.getCategory()).thenReturn(category);
        when(category.getId()).thenReturn(categoryId);
        when(category.getName()).thenReturn("Salary");
        when(transaction.getType()).thenReturn(TransactionType.INCOME);
        when(transaction.getTransactionDate()).thenReturn(LocalDate.of(2026, 11, 12));
        when(transaction.isActive()).thenReturn(true);
        when(transaction.getCreatedAt()).thenReturn(Instant.parse("2026-11-12T18:00:00Z"));
        when(transaction.getUpdatedAt()).thenReturn(Instant.parse("2026-11-12T18:00:00Z"));

        mockMvc.perform(post("/api/transactions/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(transactionId.toString()))
                .andExpect(jsonPath("$.description").value("Salary"))
                .andExpect(jsonPath("$.amount").value(5000.00))
                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()))
                .andExpect(jsonPath("$.type").value("INCOME"))
                .andExpect(jsonPath("$.active").value(true));

        verify(confirmTransaction).execute(previewId);
    }

    @Test
    @DisplayName("Should return bad request when preview request is invalid")
    void shouldReturnBadRequestWhenPreviewRequestIsInvalid() throws Exception {

        String invalidRequest = """
                {
                    "description": "",
                    "amount": 5000.00,
                    "categoryId": null,
                    "transactionDate": null
                }
                """;

        mockMvc.perform(post("/api/transactions/preview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(createTransactionPreview);
    }

    @Test
    @DisplayName("Should return bad request when preview ID is missing")
    void shouldReturnBadRequestWhenPreviewIdIsMissing() throws Exception {

        String invalidRequest = """
                {
                    "previewId": null
                }
                """;

        mockMvc.perform(post("/api/transactions/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(confirmTransaction);
    }

    @DisplayName("Should return not found when transaction preview does not exist")
    void shouldReturnNotFoundWhenTransactionPreviewDoesNotExist() throws Exception {
        UUID previewId = UUID.randomUUID();

        ConfirmTransactionRequest request =
                new ConfirmTransactionRequest(previewId);

        when(confirmTransaction.execute(previewId))
                .thenThrow(new PendingTransactionPreviewNotFoundException(
                        "Transaction preview not found or expired: " + previewId
                ));

        mockMvc.perform(post("/api/transactions/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title")
                        .value("Transaction Preview Not Found"));

        verify(confirmTransaction).execute(previewId);
    }
}
