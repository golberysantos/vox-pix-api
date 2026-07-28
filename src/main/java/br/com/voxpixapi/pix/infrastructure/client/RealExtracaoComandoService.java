package br.com.voxpixapi.pix.infrastructure.client;

import br.com.voxpixapi.pix.domain.model.ComandoPix;
import br.com.voxpixapi.pix.domain.service.ExtracaoComandoService;
import br.com.voxpixapi.pix.infrastructure.client.dto.GeminiRequest;
import br.com.voxpixapi.pix.infrastructure.client.dto.GeminiResponse;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementacao Real do Servico de Interpretacao e Extracao de Comandos (Gemini).
 *
 * Ativa apenas no perfil "prod", enviando os textos transcritos para a API
 * do Gemini 1.5 Flash e processando o JSON estruturado retornado.
 *
 * @author Golbery Santos
 */
@Service
@Profile("prod")
public class RealExtracaoComandoService implements ExtracaoComandoService {

    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper;

    @Value("${api.gemini.key}")
    private String apiKey;

    public RealExtracaoComandoService(GeminiClient geminiClient, ObjectMapper objectMapper) {
        this.geminiClient = geminiClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public ComandoPix extrair(String textoComando) {
        String prompt = "Analise a seguinte transcricao de comando de voz bancario e extraia a intencao, o valor e o destinatario do Pix.\n"
                + "Texto a analisar: \"" + textoComando + "\"";

        // Configura o payload estruturado (JSON Schema) para o Gemini
        var request = new GeminiRequest(
                List.of(new GeminiRequest.Content(
                        List.of(new GeminiRequest.Part(prompt))
                )),
                new GeminiRequest.GenerationConfig(
                        "application/json",
                        new GeminiRequest.ResponseSchema(
                                "OBJECT",
                                new GeminiRequest.Properties(
                                        new GeminiRequest.PropertyType("STRING", List.of("CONSULTA_SALDO", "REALIZAR_TRANSFERENCIA", "ERRO")),
                                        new GeminiRequest.PropertyType("NUMBER"),
                                        new GeminiRequest.PropertyType("STRING"),
                                        new GeminiRequest.PropertyType("STRING")
                                ),
                                List.of("intencao")
                        )
                )
        );

        try {
            GeminiResponse response = geminiClient.gerarConteudo(apiKey, request);
            
            // Captura o texto que o Gemini respondeu (garantido como JSON valido pelo schema)
            String jsonResposta = response.candidates().getFirst().content().parts().getFirst().text();

            // Desserializa o JSON diretamente no objeto de dominio
            return objectMapper.readValue(jsonResposta, ComandoPix.class);
        } catch (Exception e) {
            // Em caso de qualquer falha de parse ou de rede, encapsula em um ComandoPix de erro
            return new ComandoPix(
                    ComandoPix.Intencao.ERRO, 
                    null, 
                    null, 
                    "Erro ao analisar o comando de voz no Gemini: " + e.getMessage()
            );
        }
    }
}
