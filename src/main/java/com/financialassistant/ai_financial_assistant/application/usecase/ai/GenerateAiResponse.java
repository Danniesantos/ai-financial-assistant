package com.financialassistant.ai_financial_assistant.application.usecase.ai;

import com.financialassistant.ai_financial_assistant.application.port.AiAssistant;
import org.springframework.stereotype.Service;

@Service
public class GenerateAiResponse {

    private final AiAssistant aiAssistant;

    public GenerateAiResponse(AiAssistant aiAssistant) {
        this.aiAssistant = aiAssistant;
    }

    public String execute(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("Prompt is required");
        }

        return aiAssistant.generateResponse(prompt);
    }
}
