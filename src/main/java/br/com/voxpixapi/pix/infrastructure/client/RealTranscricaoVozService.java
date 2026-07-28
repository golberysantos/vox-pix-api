package br.com.voxpixapi.pix.infrastructure.client;

import br.com.voxpixapi.pix.domain.service.TranscricaoVozService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementacao Real do Servico de Transcricao de Voz (Speech-to-Text).
 *
 * Ativa apenas no perfil "prod", conectando-se a API do Whisper (Groq)
 * por meio do cliente Feign.
 *
 * @author Golbery Santos
 */
@Service
@Profile("prod")
public class RealTranscricaoVozService implements TranscricaoVozService {

    private final WhisperClient whisperClient;

    @Value("${api.groq.key}")
    private String apiKey;

    public RealTranscricaoVozService(WhisperClient whisperClient) {
        this.whisperClient = whisperClient;
    }

    @Override
    public String transcrever(MultipartFile arquivoAudio) {
        try {
            // Chama a API remota do Whisper informando o token de autorizacao
            var response = whisperClient.transcrever(
                    "Bearer " + apiKey, 
                    arquivoAudio, 
                    "whisper-large-v3"
            );
            return response.text();
        } catch (Exception e) {
            throw new RuntimeException("Erro de comunicacao com o servico Whisper: " + e.getMessage(), e);
        }
    }
}
