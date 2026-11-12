package com.financialassistant.ai_financial_assistant.infrastructure.ai;

import com.financialassistant.ai_financial_assistant.application.exception.AiProviderException;
import com.financialassistant.ai_financial_assistant.application.port.AiAssistant;
import com.financialassistant.ai_financial_assistant.infrastructure.ai.tools.FinancialSummaryTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class OpenAiAssistantAdapter implements AiAssistant {

    private final ChatClient chatClient;
    private final FinancialSummaryTool financialSummaryTool;

    private static final String SYSTEM_INSTRUCTIONS = """
            You are a financial assistant specialized in personal finance.
            
            Your responsibilities:
            - Explain personal finance concepts clearly.
            - Help users understand income, expenses and budgeting.
            - Provide practical suggestions for financial organization.
            - Communicate in the same language used by the user.
            - Use the available financial tools when the user asks about
              their actual financial data.
            
            Important rules:
            - Be clear, objective and respectful.
            - Never invent financial values or transaction records.
            - Never claim to have accessed financial data unless it was
              explicitly provided through the application.
            - When financial information is unavailable, say so clearly.
            - Do not present financial suggestions as guaranteed results.
            """;

    public OpenAiAssistantAdapter(
            ChatClient.Builder chatClientBuilder,
            FinancialSummaryTool financialSummaryTool
    ) {
        this.chatClient = chatClientBuilder.build();
        this.financialSummaryTool = financialSummaryTool;
    }

    @Override
    public String generateResponse(String prompt) {
        try {
            return chatClient.prompt()
                    .system(SYSTEM_INSTRUCTIONS)
                    .user(prompt)
                    .tools(financialSummaryTool)
                    .call()
                    .content();
        } catch (RuntimeException exception) {
            throw new AiProviderException(
                    "The AI provider is currently unavailable.",
                    exception
            );
        }
    }
}
