package com.financialassistant.ai_financial_assistant.application.usecase.ai;

import com.financialassistant.ai_financial_assistant.application.port.AiAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GenerateAiResponseTest {

    private AiAssistant aiAssistant;
    private GenerateAiResponse generateAiResponse;

    @BeforeEach
    void setUp() {
        aiAssistant = mock(AiAssistant.class);
        generateAiResponse = new GenerateAiResponse(aiAssistant);
    }

    @Test
    void shouldGenerateAiResponse() {
        String prompt = "Explain monthly expenses.";
        String expectedResponse = "Monthly expenses should be tracked.";

        when(aiAssistant.generateResponse(prompt))
                .thenReturn(expectedResponse);

        String response = generateAiResponse.execute(prompt);

        assertThat(response).isEqualTo(expectedResponse);
        verify(aiAssistant).generateResponse(prompt);
    }

    @Test
    void shouldThrowExceptionWhenPromptIsNull() {
        assertThatThrownBy(() -> generateAiResponse.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Prompt is required");

        verifyNoInteractions(aiAssistant);
    }

    @Test
    void shouldThrowExceptionWhenPromptIsBlank() {
        assertThatThrownBy(() -> generateAiResponse.execute("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Prompt is required");

        verifyNoInteractions(aiAssistant);
    }
}