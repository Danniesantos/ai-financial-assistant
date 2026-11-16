package com.financialassistant.ai_financial_assistant.infrastructure.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenAiAudioTranscriberAdapterTest {

    @Mock
    private TranscriptionModel transcriptionModel;

    @InjectMocks
    private OpenAiAudioTranscriberAdapter adapter;

    @Test
    void shouldTranscribeAudioSuccessfully() {
        byte[] audio = "audio-content".getBytes();
        String filename = "recording.mp3";
        String expectedTranscript = "I spent 150 reais on groceries.";

        when(transcriptionModel.transcribe(any(Resource.class)))
                .thenReturn(expectedTranscript);

        String result = adapter.transcribe(audio, filename);

        assertEquals(expectedTranscript, result);

        ArgumentCaptor<Resource> resourceCaptor =
                ArgumentCaptor.forClass(Resource.class);

        verify(transcriptionModel).transcribe(resourceCaptor.capture());

        assertEquals(filename, resourceCaptor.getValue().getFilename());
    }

    @Test
    void shouldThrowAiProviderExceptionWhenTranscriptionFails() {
        byte[] audio = "audio-content".getBytes();

        when(transcriptionModel.transcribe(any(Resource.class)))
                .thenThrow(new RuntimeException("Provider unavailable"));

        assertThrows(
                com.financialassistant.ai_financial_assistant.application.exception.AiProviderException.class,
                () -> adapter.transcribe(audio, "recording.mp3")
        );

        verify(transcriptionModel).transcribe(any(Resource.class));
    }

}