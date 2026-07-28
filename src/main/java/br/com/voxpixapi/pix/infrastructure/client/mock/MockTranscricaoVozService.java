package br.com.voxpixapi.pix.infrastructure.client.mock;

import br.com.voxpixapi.pix.domain.service.TranscricaoVozService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Simulador de Servico de Transcricao de Voz (Mock Service).
 *
 * Ativado apenas no perfil "dev". Retorna transcricoes ficticias de forma
 * dinamica baseadas no nome do arquivo enviado para permitir testes offline.
 *
 * @author Golbery Santos
 */
@Service
@Profile("dev")
public class MockTranscricaoVozService implements TranscricaoVozService {

    @Override
    public String transcrever(MultipartFile arquivoAudio) {
        String nomeArquivo = arquivoAudio.getOriginalFilename() != null 
                ? arquivoAudio.getOriginalFilename().toLowerCase() 
                : "";

        if (nomeArquivo.contains("saldo")) {
            return "Quero ver meu saldo, por favor";
        }
        if (nomeArquivo.contains("maria")) {
            return "Transfira 150 reais para a Maria";
        }
        if (nomeArquivo.contains("joao") || nomeArquivo.contains("joão")) {
            return "Faca um Pix de 300 reais para o Joao";
        }

        // Caso padrao de fallback
        return "Transfira 150 reais para a Maria";
    }
}
