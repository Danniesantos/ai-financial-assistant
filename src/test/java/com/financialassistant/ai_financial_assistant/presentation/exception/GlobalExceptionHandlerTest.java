package com.financialassistant.ai_financial_assistant.presentation.exception;

import com.financialassistant.ai_financial_assistant.application.exception.CategoryNotFoundException;
import com.financialassistant.ai_financial_assistant.application.exception.FinancialTransactionNotFoundException;
import com.financialassistant.ai_financial_assistant.domain.exception.InvalidCategoryException;
import com.financialassistant.ai_financial_assistant.domain.exception.InvalidFinancialTransactionException;
import com.financialassistant.ai_financial_assistant.domain.exception.InvalidMoneyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @RestController
    static class TestController {

        @GetMapping("/test/domain")
        public void throwDomainException() {
            throw new InvalidCategoryException("Category is inactive");
        }

        @GetMapping("/test/transaction")
        public void throwTransactionException() {
            throw new InvalidFinancialTransactionException(
                    "Invalid financial transaction"
            );
        }

        @GetMapping("/test/money")
        public void throwMoneyException() {
            throw new InvalidMoneyException("Invalid amount");
        }

        @GetMapping("/test/category-not-found")
        public void throwCategoryNotFoundException() {
            throw new CategoryNotFoundException("Category not found");
        }

        @GetMapping("/test/transaction-not-found")
        public void throwTransactionNotFoundException() {
            throw new FinancialTransactionNotFoundException(
                    "Financial transaction not found"
            );
        }
    }

    @Test
    void shouldReturnBadRequestWhenDomainExceptionOccurs() throws Exception {
        mockMvc.perform(get("/test/domain"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Category is inactive"))
                .andExpect(jsonPath("$.instance").value("/test/domain"));
    }

    @Test
    void shouldReturnBadRequestWhenFinancialTransactionIsInvalid() throws Exception {
        mockMvc.perform(get("/test/transaction"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid financial transaction"))
                .andExpect(jsonPath("$.instance").value("/test/transaction"));
    }

    @Test
    void shouldReturnBadRequestWhenMoneyIsInvalid() throws Exception {
        mockMvc.perform(get("/test/money"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid amount"))
                .andExpect(jsonPath("$.instance").value("/test/money"));
    }

    @Test
    void shouldReturnNotFoundWhenCategoryDoesNotExist() throws Exception {
        mockMvc.perform(get("/test/category-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Category not found"))
                .andExpect(jsonPath("$.instance").value("/test/category-not-found"));
    }

    @Test
    void shouldReturnNotFoundWhenTransactionDoesNotExist() throws Exception {
        mockMvc.perform(get("/test/transaction-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Financial transaction not found"))
                .andExpect(jsonPath("$.instance").value("/test/transaction-not-found"));
    }
}