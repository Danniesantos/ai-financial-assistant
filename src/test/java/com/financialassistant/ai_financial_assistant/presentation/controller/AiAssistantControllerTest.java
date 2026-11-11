package com.financialassistant.ai_financial_assistant.presentation.controller;

import com.financialassistant.ai_financial_assistant.application.exception.AiProviderException;
import com.financialassistant.ai_financial_assistant.application.usecase.ai.GenerateAiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AiAssistantController.class)
class AiAssistantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenerateAiResponse generateAiResponse;

    @Test
    void shouldReturnAiResponse() throws Exception {
        when(generateAiResponse.execute("Explain monthly expenses."))
                .thenReturn("Tracking expenses helps control your finances.");

        mockMvc.perform(post("/api/assistant/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "prompt": "Explain monthly expenses."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response")
                        .value("Tracking expenses helps control your finances."));

        verify(generateAiResponse).execute("Explain monthly expenses.");
    }

    @Test
    void shouldReturnBadRequestWhenPromptIsBlank() throws Exception {
        mockMvc.perform(post("/api/assistant/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "prompt": "   "
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(generateAiResponse);
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/assistant/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "prompt":
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(generateAiResponse);
    }

    @Test
    void shouldReturnServiceUnavailableWhenAiProviderFails() throws Exception {
        when(generateAiResponse.execute("Explain my expenses."))
                .thenThrow(new AiProviderException(
                        "The AI provider is currently unavailable.",
                        new RuntimeException("API error")
                ));

        mockMvc.perform(post("/api/assistant/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "prompt": "Explain my expenses."
                                }
                                """))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.title").value("Service Unavailable"))
                .andExpect(jsonPath("$.detail")
                        .value("The AI service is temporarily unavailable."));
    }
}