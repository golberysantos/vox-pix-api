package br.com.voxpixapi.pix.infrastructure.client;

import br.com.voxpixapi.pix.infrastructure.client.dto.GeminiRequest;
import br.com.voxpixapi.pix.infrastructure.client.dto.GeminiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Cliente HTTP declarativo (OpenFeign) para integracao com a API do Google Gemini.
 *
 * Ativo apenas no perfil de producao/integracao real.
 *
 * @author Golbery Santos
 */
@FeignClient(name = "gemini-client", url = "${api.gemini.url}")
@Profile("prod")
public interface GeminiClient {

    /**
     * Envia o prompt estruturado para extracao de intencao do comando.
     *
     * @param apiKey Chave de API do Google AI Studio.
     * @param request Payload contendo o prompt e as configuracoes do schema JSON.
     * @return O retorno da chamada da API do Gemini.
     */
    @PostMapping("/v1beta/models/gemini-1.5-flash:generateContent")
    GeminiResponse gerarConteudo(
            @RequestParam("key") String apiKey,
            @RequestBody GeminiRequest request
    );
}
