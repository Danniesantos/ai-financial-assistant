package com.financialassistant.ai_financial_assistant.presentation.controller;

import com.financialassistant.ai_financial_assistant.application.dto.FinancialSummary;
import com.financialassistant.ai_financial_assistant.application.usecase.transaction.*;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.presentation.dto.request.CreateFinancialTransactionRequest;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.FinancialSummaryResponse;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.FinancialTransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(
        name = "Financial Transactions",
        description = "Endpoints for financial transaction management and summaries"
)
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
    private final ConfirmTransaction confirmTransaction;

    public FinancialTransactionController(
            CreateFinancialTransaction createFinancialTransaction,
            GetFinancialTransactions getFinancialTransactions,
            GetFinancialTransactionById getFinancialTransactionById,
            GetActiveFinancialTransactions getActiveFinancialTransactions,
            DeactivateFinancialTransaction deactivateFinancialTransaction,
            ActivateFinancialTransaction activateFinancialTransaction,
            GetFinancialSummary getFinancialSummary,
            ConfirmTransaction confirmTransaction

    ) {
        this.createFinancialTransaction = createFinancialTransaction;
        this.getFinancialTransactions = getFinancialTransactions;
        this.getFinancialTransactionById = getFinancialTransactionById;
        this.getActiveFinancialTransactions = getActiveFinancialTransactions;
        this.deactivateFinancialTransaction = deactivateFinancialTransaction;
        this.activateFinancialTransaction = activateFinancialTransaction;
        this.getFinancialSummary = getFinancialSummary;
        this.confirmTransaction = confirmTransaction;
    }

    @Operation(
            summary = "Create a financial transaction",
            description = "Creates a new income or expense transaction."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
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

    @Operation(
            summary = "Confirm a transaction preview",
            description = "Confirms a previously generated transaction preview and persists the transaction."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transaction confirmed and created successfully"),
            @ApiResponse(responseCode = "404", description = "Preview not found or expired")
    })
    @PostMapping("/confirm/{previewId}")
    public ResponseEntity<FinancialTransactionResponse> confirm(
            @PathVariable UUID previewId
    ) {
        FinancialTransaction transaction = confirmTransaction.execute(previewId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FinancialTransactionResponse.from(transaction));
    }

    @Operation(
            summary = "List all financial transactions",
            description = "Returns all registered financial transactions."
    )
    @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    @GetMapping
    public ResponseEntity<List<FinancialTransactionResponse>> findAll() {
        var transactions = getFinancialTransactions.execute()
                .stream()
                .map(FinancialTransactionResponse::from)
                .toList();

        return ResponseEntity.ok(transactions);
    }

    @Operation(
            summary = "List active financial transactions",
            description = "Returns only active financial transactions."
    )
    @ApiResponse(responseCode = "200", description = "Active transactions retrieved successfully")
    @GetMapping("/active")
    public ResponseEntity<List<FinancialTransactionResponse>> findAllActive() {
        var transactions = getActiveFinancialTransactions.execute()
                .stream()
                .map(FinancialTransactionResponse::from)
                .toList();

        return ResponseEntity.ok(transactions);
    }

    @Operation(
            summary = "Get financial summary",
            description = "Calculates income, expenses, and balance for the specified date range."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Financial summary generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date range")
    })
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

    @Operation(
            summary = "Get a financial transaction by ID",
            description = "Returns the details of a specific financial transaction."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/{transactionId}")
    public ResponseEntity<FinancialTransactionResponse> findById(
            @PathVariable UUID transactionId
    ) {
        var transaction = getFinancialTransactionById.execute(transactionId);

        return ResponseEntity.ok(
                FinancialTransactionResponse.from(transaction)
        );
    }

    @Operation(
            summary = "Deactivate a financial transaction",
            description = "Deactivates an existing financial transaction."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PatchMapping("/{transactionId}/deactivate")
    public ResponseEntity<FinancialTransactionResponse> deactivate(
            @PathVariable UUID transactionId
    ) {
        var transaction = deactivateFinancialTransaction.execute(transactionId);

        return ResponseEntity.ok(
                FinancialTransactionResponse.from(transaction)
        );
    }

    @Operation(
            summary = "Activate a financial transaction",
            description = "Activates a previously deactivated financial transaction."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction activated successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
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
