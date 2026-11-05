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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivateFinancialTransactionTest {

    @Mock
    private FinancialTransactionRepository transactionRepository;

    @InjectMocks
    private ActivateFinancialTransaction activateFinancialTransaction;

    @Test
    @DisplayName("Should activate financial transaction successfully")
    void shouldActivateFinancialTransactionSuccessfully() {

        FinancialTransaction transaction = createTransaction();
        transaction.deactivate();

        UUID transactionId = transaction.getId();

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        when(transactionRepository.save(transaction))
                .thenReturn(transaction);

        FinancialTransaction result =
                activateFinancialTransaction.execute(transactionId);

        assertNotNull(result);
        assertTrue(result.isActive());

        verify(transactionRepository).findById(transactionId);
        verify(transactionRepository).save(transaction);
    }

    @Test
    @DisplayName("Should throw exception when transaction does not exist")
    void shouldThrowExceptionWhenTransactionDoesNotExist() {

        UUID transactionId = UUID.randomUUID();

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.empty());

        assertThrows(
                FinancialTransactionNotFoundException.class,
                () -> activateFinancialTransaction.execute(transactionId)
        );

        verify(transactionRepository).findById(transactionId);
    }

    @Test
    @DisplayName("Should not save transaction when it does not exist")
    void shouldNotSaveTransactionWhenItDoesNotExist() {

        UUID transactionId = UUID.randomUUID();

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.empty());

        assertThrows(
                FinancialTransactionNotFoundException.class,
                () -> activateFinancialTransaction.execute(transactionId)
        );

        verify(transactionRepository, never())
                .save(any(FinancialTransaction.class));
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