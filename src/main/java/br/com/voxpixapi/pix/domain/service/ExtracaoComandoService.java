package br.com.voxpixapi.pix.domain.service;

import br.com.voxpixapi.pix.domain.model.ComandoPix;

/**
 * Interface do Dominio (Porta) para o Servico de Extracao de Comandos (NLP / IA).
 *
 * @author Golbery Santos
 */
public interface ExtracaoComandoService {

    /**
     * Analisa o texto em linguagem natural e extrai a intencao estruturada do comando.
     *
     * @param textoComando O texto transcrito da ordem do cliente.
     * @return O objeto ComandoPix estruturado.
     */
    ComandoPix extrair(String textoComando);
}
