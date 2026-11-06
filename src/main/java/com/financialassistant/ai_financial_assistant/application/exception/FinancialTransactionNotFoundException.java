package com.financialassistant.ai_financial_assistant.application.exception;

public class FinancialTransactionNotFoundException extends RuntimeException {
    public FinancialTransactionNotFoundException(String message) {
        super(message);
    }
}
