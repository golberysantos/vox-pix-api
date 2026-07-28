package br.com.voxpixapi.pix.domain.model;

import br.com.voxpixapi.conta.domain.model.Conta;
import br.com.voxpixapi.conta.domain.model.Transacao;
import java.math.BigDecimal;

/**
 * Record de Dominio representando o resultado unificado do processamento de comandos por voz.
 *
 * @author Golbery Santos
 */
public record ProcessamentoVozResult(
        String transcricao,
        ComandoPix.Intencao intencao,
        String mensagemRetorno,
        boolean sucesso,
        BigDecimal saldoAtual,
        TransacaoInfo transacao
) {

    public record TransacaoInfo(
            String numeroContaOrigem,
            String numeroContaDestino,
            String titularDestino,
            BigDecimal valor
    ) {}

    public static ProcessamentoVozResult criarConsultaSaldo(String transcricao, Conta conta) {
        return new ProcessamentoVozResult(
                transcricao,
                ComandoPix.Intencao.CONSULTA_SALDO,
                "Seu saldo atual e de R$ " + conta.getSaldo(),
                true,
                conta.getSaldo(),
                null
        );
    }

    public static ProcessamentoVozResult criarTransferencia(String transcricao, Transacao transacao) {
        var info = new TransacaoInfo(
                transacao.getContaOrigem().getNumeroConta(),
                transacao.getContaDestino().getNumeroConta(),
                transacao.getContaDestino().getTitular(),
                transacao.getValor()
        );
        return new ProcessamentoVozResult(
                transcricao,
                ComandoPix.Intencao.REALIZAR_TRANSFERENCIA,
                "Pix de R$ " + transacao.getValor() + " realizado com sucesso para " + transacao.getContaDestino().getTitular() + "!",
                true,
                transacao.getContaOrigem().getSaldo(),
                info
        );
    }

    public static ProcessamentoVozResult criarErro(String transcricao, String erro) {
        return new ProcessamentoVozResult(
                transcricao,
                ComandoPix.Intencao.ERRO,
                erro,
                false,
                null,
                null
        );
    }
}
