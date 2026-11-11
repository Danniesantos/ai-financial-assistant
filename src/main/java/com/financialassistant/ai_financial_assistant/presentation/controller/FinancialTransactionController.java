package com.financialassistant.ai_financial_assistant.presentation.controller;

import com.financialassistant.ai_financial_assistant.application.dto.FinancialSummary;
import com.financialassistant.ai_financial_assistant.application.usecase.transaction.*;
import com.financialassistant.ai_financial_assistant.presentation.dto.request.CreateFinancialTransactionRequest;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.FinancialSummaryResponse;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.FinancialTransactionResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class FinancialTransactionController {

    private final CreateFinancialTransaction createFinancialTransaction;
    private final GetFinancialTransactions getFinancialTransactions;
    private final GetFinancialTransactionById getFinancialTransactionById;
    private final GetActiveFinancialTransactions getActiveFinancialTransactions;
    private final DeactivateFinancialTransaction deactivateFinancialTransaction;
    private final ActivateFinancialTransaction activateFinancialTransaction;
    private final GetFinancialSummary getFinancialSummary;

    public FinancialTransactionController(
            CreateFinancialTransaction createFinancialTransaction,
            GetFinancialTransactions getFinancialTransactions,
            GetFinancialTransactionById getFinancialTransactionById,
            GetActiveFinancialTransactions getActiveFinancialTransactions,
            DeactivateFinancialTransaction deactivateFinancialTransaction,
            ActivateFinancialTransaction activateFinancialTransaction,
            GetFinancialSummary getFinancialSummary

    ) {
        this.createFinancialTransaction = createFinancialTransaction;
        this.getFinancialTransactions = getFinancialTransactions;
        this.getFinancialTransactionById = getFinancialTransactionById;
        this.getActiveFinancialTransactions = getActiveFinancialTransactions;
        this.deactivateFinancialTransaction = deactivateFinancialTransaction;
        this.activateFinancialTransaction = activateFinancialTransaction;
        this.getFinancialSummary = getFinancialSummary;
    }

    @PostMapping
    public ResponseEntity<FinancialTransactionResponse> create(
            @Valid @RequestBody CreateFinancialTransactionRequest request
    ) {
        var transaction = createFinancialTransaction.execute(
                request.description(),
                request.amount(),
                request.categoryId(),
                request.transactionDate()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(FinancialTransactionResponse.from(transaction));
    }

    @GetMapping
    public ResponseEntity<List<FinancialTransactionResponse>> findAll() {
        var transactions = getFinancialTransactions.execute()
                .stream()
                .map(FinancialTransactionResponse::from)
                .toList();

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/active")
    public ResponseEntity<List<FinancialTransactionResponse>> findAllActive() {
        var transactions = getActiveFinancialTransactions.execute()
                .stream()
                .map(FinancialTransactionResponse::from)
                .toList();

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/summary")
    public ResponseEntity<FinancialSummaryResponse> getSummary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        FinancialSummary summary =
                getFinancialSummary.execute(startDate, endDate);

        return ResponseEntity.ok(
                FinancialSummaryResponse.from(summary)
        );
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<FinancialTransactionResponse> findById(
            @PathVariable UUID transactionId
    ) {
        var transaction = getFinancialTransactionById.execute(transactionId);

        return ResponseEntity.ok(
                FinancialTransactionResponse.from(transaction)
        );
    }

    @PatchMapping("/{transactionId}/deactivate")
    public ResponseEntity<FinancialTransactionResponse> deactivate(
            @PathVariable UUID transactionId
    ) {
        var transaction = deactivateFinancialTransaction.execute(transactionId);

        return ResponseEntity.ok(
                FinancialTransactionResponse.from(transaction)
        );
    }

    @PatchMapping("/{transactionId}/activate")
    public ResponseEntity<FinancialTransactionResponse> activate(
            @PathVariable UUID transactionId
    ) {
        var transaction = activateFinancialTransaction.execute(transactionId);

        return ResponseEntity.ok(
                FinancialTransactionResponse.from(transaction)
        );
    }
}
