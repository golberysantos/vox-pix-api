package br.com.voxpixapi.pix.domain.service;

import br.com.voxpixapi.conta.domain.model.Conta;
import br.com.voxpixapi.conta.domain.model.Transacao;
import br.com.voxpixapi.conta.domain.service.BankingService;
import br.com.voxpixapi.pix.domain.model.ComandoPix;
import br.com.voxpixapi.pix.domain.model.ProcessamentoVozResult;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

/**
 * Orquestrador do Dominio (Use Case) de processamento de comandos de voz.
 *
 * Coleta a voz, transcreve, interpreta a intencao e dispara a regra de negocio
 * bancaria correspondente.
 *
 * @author Golbery Santos
 */
public class VoiceCommandService {

    private final TranscricaoVozService transcricaoVozService;
    private final ExtracaoComandoService extracaoComandoService;
    private final BankingService bankingService;

    public VoiceCommandService(TranscricaoVozService transcricaoVozService, 
                               ExtracaoComandoService extracaoComandoService, 
                               BankingService bankingService) {
        this.transcricaoVozService = transcricaoVozService;
        this.extracaoComandoService = extracaoComandoService;
        this.bankingService = bankingService;
    }

    /**
     * Processa a voz e executa a transacao ou consulta.
     *
     * @param cpfAutenticado CPF do emissor autenticado.
     * @param arquivoAudio O arquivo com a fala gravada.
     * @return O resultado da orquestracao em formato unificado.
     */
    public ProcessamentoVozResult processarComandoVoz(String cpfAutenticado, MultipartFile arquivoAudio) {
        // 1. Transcricao de Voz (Speech-to-Text)
        String transcricao = transcricaoVozService.transcrever(arquivoAudio);

        // 2. Extracao de Intencao (NLP / IA)
        ComandoPix comando = extracaoComandoService.extrair(transcricao);

        // 3. Orquestracao Bancaria
        switch (comando.intencao()) {
            case CONSULTA_SALDO:
                Optional<Conta> contaOpt = bankingService.buscarPorCpf(cpfAutenticado);
                if (contaOpt.isEmpty()) {
                    return ProcessamentoVozResult.criarErro(transcricao, "Conta nao encontrada para o CPF autenticado.");
                }
                return ProcessamentoVozResult.criarConsultaSaldo(transcricao, contaOpt.get());

            case REALIZAR_TRANSFERENCIA:
                try {
                    Transacao transacao = bankingService.realizarTransferencia(
                            cpfAutenticado, 
                            comando.destinatario(), 
                            comando.valor()
                    );
                    if (transacao.getStatus() == Transacao.StatusTransacao.FALHA) {
                        return ProcessamentoVozResult.criarErro(transcricao, transacao.getDescricaoErro());
                    }
                    return ProcessamentoVozResult.criarTransferencia(transcricao, transacao);
                } catch (Exception e) {
                    return ProcessamentoVozResult.criarErro(transcricao, "Erro ao processar a transferencia: " + e.getMessage());
                }

            case ERRO:
            default:
                return ProcessamentoVozResult.criarErro(transcricao, 
                        comando.mensagemErro() != null ? comando.mensagemErro() : "Comando nao compreendido.");
        }
    }
}
