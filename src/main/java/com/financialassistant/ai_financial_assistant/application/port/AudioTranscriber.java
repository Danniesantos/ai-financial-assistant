package com.financialassistant.ai_financial_assistant.application.port;

import org.springframework.web.multipart.MultipartFile;

public interface AudioTranscriber {

    String transcribe(byte[] audio, String filename);
}
