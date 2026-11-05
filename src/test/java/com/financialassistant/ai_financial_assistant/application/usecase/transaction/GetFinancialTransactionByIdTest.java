package com.financialassistant.ai_financial_assistant.application.usecase.transaction;


import com.financialassistant.ai_financial_assistant.application.exception.FinancialTransactionNotFoundException;
import com.financialassistant.ai_financial_assistant.domain.enums.TransactionType;
import com.financialassistant.ai_financial_assistant.domain.model.Category;
import com.financialassistant.ai_financial_assistant.domain.model.FinancialTransaction;
import com.financialassistant.ai_financial_assistant.domain.repository.FinancialTransactionRepository;
import com.financialassistant.ai_financial_assistant.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFinancialTransactionByIdTest {

    @Mock
    private FinancialTransactionRepository transactionRepository;

    @InjectMocks
    private GetFinancialTransactionById getFinancialTransactionById;

    @Test
    @DisplayName("Should return financial transaction when ID exists")
    void shouldReturnFinancialTransactionWhenIdExists() {

        FinancialTransaction transaction = createTransaction();

        UUID transactionId = transaction.getId();

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        FinancialTransaction result =
                getFinancialTransactionById.execute(transactionId);

        assertNotNull(result);
        assertEquals(transaction, result);

        verify(transactionRepository).findById(transactionId);
    }

    @Test
    @DisplayName("Should throw exception when transaction does not exist")
    void shouldThrowExceptionWhenTransactionDoesNotExist() {

        FinancialTransaction transaction = createTransaction();

        UUID transactionId = transaction.getId();

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.empty());

        assertThrows(
                FinancialTransactionNotFoundException.class,
                () -> getFinancialTransactionById.execute(transactionId)
        );

        verify(transactionRepository).findById(transactionId);
    }

    private FinancialTransaction createTransaction() {
        Category category = new Category(
                "Food",
                TransactionType.EXPENSE,
                false
        );

        return new FinancialTransaction(
                "Supermarket purchase",
                new Money(new BigDecimal("150.00")),
                category,
                LocalDate.of(2026, 11, 3)
        );
    }

}