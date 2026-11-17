package com.financialassistant.ai_financial_assistant.presentation.controller;

import com.financialassistant.ai_financial_assistant.application.dto.PendingTransactionPreview;
import com.financialassistant.ai_financial_assistant.application.usecase.transaction.ConfirmTransaction;
import com.financialassistant.ai_financial_assistant.application.usecase.transaction.CreateTransactionPreview;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.presentation.dto.request.ConfirmTransactionRequest;
import com.financialassistant.ai_financial_assistant.presentation.dto.request.CreateFinancialTransactionRequest;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.FinancialTransactionResponse;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.TransactionPreviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Transaction Preview",
        description = "Endpoints for transaction preview and explicit confirmation"
)
@RestController
@RequestMapping("/api/transactions")
public class TransactionPreviewController {

    private final CreateTransactionPreview createTransactionPreview;
    private final ConfirmTransaction confirmTransaction;

    public TransactionPreviewController(
            CreateTransactionPreview createTransactionPreview,
            ConfirmTransaction confirmTransaction
    ) {
        this.createTransactionPreview = createTransactionPreview;
        this.confirmTransaction = confirmTransaction;
    }

    @Operation(
            summary = "Create a transaction preview",
            description = "Prepares a transaction for review without persisting it."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction preview created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PostMapping("/preview")
    public ResponseEntity<TransactionPreviewResponse> createPreview(
            @Valid @RequestBody CreateFinancialTransactionRequest request
    ) {
        PendingTransactionPreview preview = createTransactionPreview.execute(
                request.description(),
                request.amount(),
                request.categoryId(),
                request.transactionDate()
        );

        return ResponseEntity.ok(TransactionPreviewResponse.from(preview));
    }

    @Operation(
            summary = "Confirm a transaction preview",
            description = "Confirms a previously created preview and persists the transaction after explicit user confirmation."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transaction confirmed and created successfully"),
            @ApiResponse(responseCode = "404", description = "Preview not found or expired")
    })
    @PostMapping("/confirm")
    public ResponseEntity<FinancialTransactionResponse> confirmTransaction(
            @Valid @RequestBody ConfirmTransactionRequest request
    ) {
        FinancialTransaction transaction = confirmTransaction.execute(request.previewId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FinancialTransactionResponse.from(transaction));
    }
}
