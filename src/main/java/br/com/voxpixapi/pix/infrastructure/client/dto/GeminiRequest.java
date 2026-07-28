package br.com.voxpixapi.pix.infrastructure.client.dto;

import java.util.List;

/**
 * DTO para formatar a requisicao estruturada enviada para a API do Gemini.
 *
 * Utiliza o recurso de "Structured Output" (JSON Schema) para forcar o Gemini
 * a responder exatamente no formato do nosso record ComandoPix, eliminando chatters.
 *
 * @author Golbery Santos
 */
public record GeminiRequest(
        List<Content> contents,
        GenerationConfig generationConfig
) {
    public record Content(List<Part> parts) {}
    
    public record Part(String text) {}
    
    public record GenerationConfig(
            String responseMimeType,
            ResponseSchema responseSchema
    ) {}
    
    public record ResponseSchema(
            String type,
            Properties properties,
            List<String> required
    ) {}
    
    public record Properties(
            PropertyType intencao,
            PropertyType valor,
            PropertyType destinatario,
            PropertyType mensagemErro
    ) {}
    
    public record PropertyType(
            String type,
            List<String> enumeration
    ) {
        public PropertyType(String type) {
            this(type, null);
        }
    }
}
