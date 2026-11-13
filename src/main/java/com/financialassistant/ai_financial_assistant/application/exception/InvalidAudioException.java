package com.financialassistant.ai_financial_assistant.application.exception;

import com.financialassistant.ai_financial_assistant.domain.exception.DomainException;

public class InvalidAudioException extends DomainException {
    public InvalidAudioException(String message) {
        super(message);
    }
}
