package com.financialassistant.ai_financial_assistant.infrastructure.ai;

import com.financialassistant.ai_financial_assistant.application.exception.AiProviderException;
import com.financialassistant.ai_financial_assistant.infrastructure.ai.tools.FinancialSummaryTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenAiAssistantAdapterTest {

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    private ChatClient.CallResponseSpec responseSpec;

    @Mock
    private FinancialSummaryTool financialSummaryTool;

    private OpenAiAssistantAdapter adapter;

    @BeforeEach
    void setUp() {
        when(chatClientBuilder.build()).thenReturn(chatClient);

        adapter = new OpenAiAssistantAdapter(
                chatClientBuilder,
                financialSummaryTool
        );
    }

    @Test
    void shouldGenerateResponseSuccessfully() {
        String prompt = "How can I organize my finances?";
        String expectedResponse = "Start by tracking your income and expenses.";

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(prompt)).thenReturn(requestSpec);
        when(requestSpec.tools(financialSummaryTool)).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn(expectedResponse);

        String result = adapter.generateResponse(prompt);

        assertThat(result).isEqualTo(expectedResponse);

        verify(chatClientBuilder).build();
        verify(chatClient).prompt();
        verify(requestSpec).system(anyString());
        verify(requestSpec).user(prompt);
        verify(requestSpec).tools(financialSummaryTool);
        verify(requestSpec).call();
        verify(responseSpec).content();
    }

    @Test
    void shouldWrapProviderException() {
        String prompt = "Show my financial summary";

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(prompt)).thenReturn(requestSpec);
        when(requestSpec.tools(financialSummaryTool)).thenReturn(requestSpec);
        when(requestSpec.call()).thenThrow(
                new RuntimeException("Provider unavailable")
        );

        assertThatThrownBy(() -> adapter.generateResponse(prompt))
                .isInstanceOf(AiProviderException.class)
                .hasMessage("The AI provider is currently unavailable.");

        verify(requestSpec).tools(financialSummaryTool);
        verify(requestSpec).call();
        verify(responseSpec, never()).content();
    }
}