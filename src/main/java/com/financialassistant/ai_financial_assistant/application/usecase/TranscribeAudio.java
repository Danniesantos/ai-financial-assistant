package com.financialassistant.ai_financial_assistant.application.usecase;

import com.financialassistant.ai_financial_assistant.application.exception.InvalidAudioException;
import com.financialassistant.ai_financial_assistant.application.port.AudioTranscriber;
import org.springframework.stereotype.Service;

@Service
public class TranscribeAudio {

    private final AudioTranscriber audioTranscriber;

    public TranscribeAudio(AudioTranscriber audioTranscriber) {
        this.audioTranscriber = audioTranscriber;
    }

    public String execute(byte[] audio, String filename) {
        if (audio == null || audio.length == 0) {
            throw new InvalidAudioException("Audio file must not be empty.");
        }

        if (filename == null || filename.isBlank()) {
            throw new InvalidAudioException("Audio filename must not be blank.");
        }

        return audioTranscriber.transcribe(audio, filename);
    }
}
