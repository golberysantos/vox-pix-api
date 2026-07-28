package br.com.voxpixapi.pix.domain.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interface do Dominio (Porta) para o Servico de Transcricao de Voz (Speech-to-Text).
 *
 * @author Golbery Santos
 */
public interface TranscricaoVozService {

    /**
     * Transcreve um arquivo de audio gravado para texto em portugues.
     *
     * @param arquivoAudio O arquivo de audio (.mp3, .wav, etc.) enviado pelo cliente.
     * @return O texto transcrito correspondente.
     */
    String transcrever(MultipartFile arquivoAudio);
}
