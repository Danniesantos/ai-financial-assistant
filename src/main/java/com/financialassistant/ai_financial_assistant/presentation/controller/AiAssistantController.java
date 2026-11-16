package com.financialassistant.ai_financial_assistant.presentation.controller;

import com.financialassistant.ai_financial_assistant.application.usecase.TranscribeAudio;
import com.financialassistant.ai_financial_assistant.application.usecase.ai.GenerateAiResponse;
import com.financialassistant.ai_financial_assistant.presentation.dto.request.AiAssistantRequest;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.AiAssistantResponse;
import com.financialassistant.ai_financial_assistant.presentation.dto.response.AudioTranscriptionResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PostMapping("/chat")
    public ResponseEntity<AiAssistantResponse> chat(
            @Valid @RequestBody AiAssistantRequest request
    ) {
        String response = generateAiResponse.execute(request.prompt());

        return ResponseEntity.ok(new AiAssistantResponse(response));
    }

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
