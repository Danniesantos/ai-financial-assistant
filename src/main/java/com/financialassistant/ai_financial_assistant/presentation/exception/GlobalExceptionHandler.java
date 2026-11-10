package com.financialassistant.ai_financial_assistant.presentation.exception;

import com.financialassistant.ai_financial_assistant.application.exception.CategoryNotFoundException;
import com.financialassistant.ai_financial_assistant.application.exception.FinancialTransactionNotFoundException;
import com.financialassistant.ai_financial_assistant.domain.exception.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> handleDomainException(
            DomainException exception,
            HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );

        problem.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problem);
    }

    @ExceptionHandler({
            CategoryNotFoundException.class,
            FinancialTransactionNotFoundException.class
    })
    public ResponseEntity<ProblemDetail> handleNotFoundException(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        problem.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Request validation failed"
        );

        problem.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("errors", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleInvalidJson(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid request body"
        );

        problem.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problem);
    }
}
