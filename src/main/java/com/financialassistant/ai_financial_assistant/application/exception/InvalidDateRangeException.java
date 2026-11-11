package com.financialassistant.ai_financial_assistant.application.exception;

import com.financialassistant.ai_financial_assistant.domain.exception.DomainException;

public class InvalidDateRangeException extends DomainException {
    public InvalidDateRangeException(String message) {
        super(message);
    }
}
