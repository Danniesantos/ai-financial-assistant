package com.financialassistant.ai_financial_assistant.presentation.controller;

import com.financialassistant.ai_financial_assistant.application.dto.PendingTransactionPreview;
import com.financialassistant.ai_financial_assistant.application.usecase.transaction.ConfirmTransaction;
import com.financialassistant.ai_financial_assistant.application.usecase.transaction.CreateTransactionPreview;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.presentation.dto.request.ConfirmTransactionRequest;
import com.financialassistant.ai_financial_assistant.presentation.dto.request.CreateFinancialTransactionRequest;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.FinancialTransactionResponse;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.TransactionPreviewResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/confirm")
    public ResponseEntity<FinancialTransactionResponse> confirmTransaction(
            @Valid @RequestBody ConfirmTransactionRequest request
    ) {
        FinancialTransaction transaction = confirmTransaction.execute(request.previewId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FinancialTransactionResponse.from(transaction));
    }
}
