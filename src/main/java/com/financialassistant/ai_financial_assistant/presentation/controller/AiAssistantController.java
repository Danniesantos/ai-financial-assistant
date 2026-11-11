package com.financialassistant.ai_financial_assistant.presentation.controller;

import com.financialassistant.ai_financial_assistant.application.usecase.ai.GenerateAiResponse;
import com.financialassistant.ai_financial_assistant.presentation.dto.request.AiAssistantRequest;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.AiAssistantResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistant")
public class AiAssistantController {

    private final GenerateAiResponse generateAiResponse;

    public AiAssistantController(GenerateAiResponse generateAiResponse) {
        this.generateAiResponse = generateAiResponse;
    }

    @PostMapping("/chat")
    public ResponseEntity<AiAssistantResponse> chat(
            @Valid @RequestBody AiAssistantRequest request
    ) {
        String response = generateAiResponse.execute(request.prompt());

        return ResponseEntity.ok(new AiAssistantResponse(response));
    }
}
