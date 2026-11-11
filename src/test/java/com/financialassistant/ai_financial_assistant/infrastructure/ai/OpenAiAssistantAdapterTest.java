package com.financialassistant.ai_financial_assistant.infrastructure.ai;

import com.financialassistant.ai_financial_assistant.application.exception.AiProviderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class OpenAiAssistantAdapterTest {

    private ChatClient.Builder chatClientBuilder;
    private ChatClient chatClient;
    private ChatClient.ChatClientRequestSpec requestSpec;
    private ChatClient.CallResponseSpec responseSpec;

    private OpenAiAssistantAdapter adapter;

    @BeforeEach
    void setUp() {
        chatClientBuilder = mock(ChatClient.Builder.class);
        chatClient = mock(ChatClient.class);
        requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        responseSpec = mock(ChatClient.CallResponseSpec.class);

        when(chatClientBuilder.build()).thenReturn(chatClient);

        adapter = new OpenAiAssistantAdapter(chatClientBuilder);
    }

    @Test
    void shouldGenerateResponse() {
        String prompt = "Explain monthly expenses.";
        String expectedResponse = "Track your expenses every month.";

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(prompt)).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn(expectedResponse);

        String response = adapter.generateResponse(prompt);

        assertThat(response).isEqualTo(expectedResponse);

        verify(chatClient).prompt();
        verify(requestSpec).system(anyString());
        verify(requestSpec).user(prompt);
        verify(requestSpec).call();
        verify(responseSpec).content();
    }

    @Test
    void shouldThrowAiProviderExceptionWhenProviderFails() {
        String prompt = "Explain monthly expenses.";

        RuntimeException originalException =
                new RuntimeException("API connection failed");

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(prompt)).thenReturn(requestSpec);
        when(requestSpec.call()).thenThrow(originalException);

        assertThatThrownBy(() -> adapter.generateResponse(prompt))
                .isInstanceOf(AiProviderException.class)
                .hasMessage("The AI provider is currently unavailable.")
                .hasCause(originalException);
    }
}