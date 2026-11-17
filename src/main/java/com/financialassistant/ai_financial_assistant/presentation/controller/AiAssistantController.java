package com.financialassistant.ai_financial_assistant.presentation.controller;

import com.financialassistant.ai_financial_assistant.application.usecase.TranscribeAudio;
import com.financialassistant.ai_financial_assistant.application.usecase.ai.GenerateAiResponse;
import com.financialassistant.ai_financial_assistant.presentation.dto.request.AiAssistantRequest;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.AiAssistantResponse;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.AudioTranscriptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(
        name = "AI Assistant",
        description = "Endpoints for AI-powered conversations and audio transcription"
)
@RestController
@RequestMapping("/api/assistant")
public class AiAssistantController {

    private final GenerateAiResponse generateAiResponse;
    private final TranscribeAudio transcribeAudio;

    public AiAssistantController(
            GenerateAiResponse generateAiResponse,
            TranscribeAudio transcribeAudio
    ) {
        this.generateAiResponse = generateAiResponse;
        this.transcribeAudio = transcribeAudio;
    }

    @Operation(
            summary = "Send a message to the AI assistant",
            description = "Processes a natural language prompt and returns an AI-generated response."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "AI response generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "503", description = "AI provider unavailable")
    })
    @PostMapping("/chat")
    public ResponseEntity<AiAssistantResponse> chat(
            @Valid @RequestBody AiAssistantRequest request
    ) {
        String response = generateAiResponse.execute(request.prompt());

        return ResponseEntity.ok(new AiAssistantResponse(response));
    }

    @Operation(
            summary = "Transcribe an audio file",
            description = "Receives an audio file and returns its transcription using OpenAI."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Audio transcribed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid audio file"),
            @ApiResponse(responseCode = "503", description = "Audio transcription provider unavailable")
    })
    @PostMapping(
            value = "/transcribe",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AudioTranscriptionResponse> transcribe(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        String transcription = transcribeAudio.execute(
                file.getBytes(),
                file.getOriginalFilename()
        );

        return ResponseEntity.ok(
                new AudioTranscriptionResponse(transcription)
        );
    }
}
