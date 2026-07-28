package br.com.voxpixapi.pix.infrastructure.client.dto;

import java.util.List;

/**
 * DTO para mapear a resposta enviada pela API do Gemini.
 *
 * @author Golbery Santos
 */
public record GeminiResponse(
        List<Candidate> candidates
) {
    public record Candidate(Content content) {}
    
    public record Content(List<Part> parts) {}
    
    public record Part(String text) {}
}
