package br.com.voxpixapi.pix.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * Cliente HTTP declarativo (OpenFeign) para integracao com a API de Transcricao.
 *
 * Ativo apenas no perfil de producao/integracao real.
 *
 * @author Golbery Santos
 */
@FeignClient(name = "whisper-client", url = "${api.groq.url}")
@Profile("prod")
public interface WhisperClient {

    /**
     * Envia um arquivo de audio multipart para transcricao.
     *
     * @param authorization Token de autorizacao (ex: "Bearer <chave_api>").
     * @param file O arquivo de audio.
     * @param model Identificador do modelo (ex: "whisper-large-v3").
     * @return DTO simplificado contendo o texto transcrito.
     */
    @PostMapping(value = "/v1/audio/transcriptions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    WhisperResponse transcrever(
            @RequestHeader("Authorization") String authorization,
            @RequestPart("file") MultipartFile file,
            @RequestPart("model") String model
    );

    record WhisperResponse(String text) {}
}
