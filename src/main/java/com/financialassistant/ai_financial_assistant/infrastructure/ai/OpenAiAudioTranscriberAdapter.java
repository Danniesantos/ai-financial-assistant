package com.financialassistant.ai_financial_assistant.infrastructure.ai;

import com.financialassistant.ai_financial_assistant.application.exception.AiProviderException;
import com.financialassistant.ai_financial_assistant.application.port.AudioTranscriber;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

@Component
public class OpenAiAudioTranscriberAdapter implements AudioTranscriber {

    private final TranscriptionModel transcriptionModel;

    public OpenAiAudioTranscriberAdapter(TranscriptionModel transcriptionModel) {
        this.transcriptionModel = transcriptionModel;
    }

    @Override
    public String transcribe(byte[] audio, String filename) {
        ByteArrayResource audioResource = new ByteArrayResource(audio) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        try {
            return transcriptionModel.transcribe(audioResource);
        } catch (RuntimeException exception) {
            throw new AiProviderException(
                    "The audio transcription provider is currently unavailable.",
                    exception
            );
        }
    }
}
