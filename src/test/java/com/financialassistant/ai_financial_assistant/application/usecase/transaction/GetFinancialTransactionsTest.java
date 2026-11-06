package com.financialassistant.ai_financial_assistant.application.usecase.transaction;

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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFinancialTransactionsTest {

    @Mock
    private FinancialTransactionRepository transactionRepository;

    @InjectMocks
    private GetFinancialTransactions getFinancialTransactions;

    @Test
    @DisplayName("Should return all financial transactions")
    void shouldReturnAllFinancialTransactions() {

        List<FinancialTransaction> transactions = List.of(
                createTransaction("Supermarket purchase", "150.00"),
                createTransaction("Monthly salary", "3000.00")
        );

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<FinancialTransaction> result = getFinancialTransactions.execute();

        assertEquals(2, result.size());
        assertEquals(transactions, result);

        verify(transactionRepository).findAll();
    }

    @Test
    @DisplayName("Should return an empty list when no transactions exist")
    void shouldReturnEmptyListWhenNoTransactionsExist() {

        when(transactionRepository.findAll()).thenReturn(List.of());

        List<FinancialTransaction> result = getFinancialTransactions.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(transactionRepository).findAll();
    }

    private FinancialTransaction createTransaction(String description, String amount) {
        Category category = new Category(
                "Food",
                TransactionType.EXPENSE,
                false
        );

        return new FinancialTransaction(
                description,
                new Money(new BigDecimal(amount)),
                category,
                LocalDate.of(2026, 11, 3)
        );
    }

}