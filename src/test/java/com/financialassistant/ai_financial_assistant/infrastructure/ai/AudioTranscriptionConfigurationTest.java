package com.financialassistant.ai_financial_assistant.infrastructure.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AudioTranscriptionConfigurationTest {

    @Autowired
    private TranscriptionModel transcriptionModel;

    @Test
    void shouldLoadTranscriptionModel() {
        assertNotNull(transcriptionModel);
    }
}
