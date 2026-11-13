package com.financialassistant.ai_financial_assistant.infrastructure.ai;

import com.financialassistant.ai_financial_assistant.application.exception.AiProviderException;
import com.financialassistant.ai_financial_assistant.application.port.AiAssistant;
import com.financialassistant.ai_financial_assistant.infrastructure.ai.tools.CreateTransactionTool;
import com.financialassistant.ai_financial_assistant.infrastructure.ai.tools.FinancialSummaryTool;
import com.financialassistant.ai_financial_assistant.infrastructure.ai.tools.QueryCategoriesTool;
import com.financialassistant.ai_financial_assistant.infrastructure.ai.tools.QueryTransactionsTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class OpenAiAssistantAdapter implements AiAssistant {

    private final ChatClient chatClient;
    private final FinancialSummaryTool financialSummaryTool;
    private final QueryTransactionsTool queryTransactionsTool;
    private final QueryCategoriesTool queryCategoriesTool;
    private final CreateTransactionTool createTransactionTool;

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

            Transaction preview and confirmation rules:
            - When the user requests a new financial transaction, use the
              transaction preview tool to prepare the transaction.
            - Present all preview details, including description, amount,
              category, type and date.
            - Never claim that a transaction has been saved when only a
              preview has been created.
            - Never attempt to confirm or save a transaction yourself.
            - The application requires explicit user confirmation through
              its confirmation endpoint before persisting the transaction.
            - Inform the user when a preview expires.
            - A confirmation must refer to the exact preview ID presented
              to the user.
            - Never assume confirmation based on ambiguous messages or
              previous approvals.
            - After presenting a preview, clearly tell the user that it
              has not been saved and provide the preview ID required
              for confirmation.
            """;

    public OpenAiAssistantAdapter(
            ChatClient.Builder chatClientBuilder,
            FinancialSummaryTool financialSummaryTool,
            QueryTransactionsTool queryTransactionsTool,
            QueryCategoriesTool queryCategoriesTool,
            CreateTransactionTool createTransactionTool
    ) {
        this.chatClient = chatClientBuilder.build();
        this.financialSummaryTool = financialSummaryTool;
        this.queryTransactionsTool = queryTransactionsTool;
        this.queryCategoriesTool = queryCategoriesTool;
        this.createTransactionTool = createTransactionTool;
    }

    @Override
    public String generateResponse(String prompt) {
        try {
            return chatClient.prompt()
                    .system(SYSTEM_INSTRUCTIONS)
                    .user(prompt)
                    .tools(
                            financialSummaryTool,
                            queryTransactionsTool,
                            queryCategoriesTool,
                            createTransactionTool
                    )
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
