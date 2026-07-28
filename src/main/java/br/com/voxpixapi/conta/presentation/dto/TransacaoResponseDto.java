package br.com.voxpixapi.conta.presentation.dto;

import br.com.voxpixapi.conta.domain.model.Transacao;
import br.com.voxpixapi.conta.domain.model.Transacao.StatusTransacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para expor dados detalhados das transacoes bancarias realizadas.
 *
 * @author Golbery Santos
 */
public record TransacaoResponseDto(
        Long id,
        String numeroContaOrigem,
        String titularOrigem,
        String numeroContaDestino,
        String titularDestino,
        BigDecimal valor,
        LocalDateTime dataHora,
        StatusTransacao status,
        String descricaoErro
) {

    public static TransacaoResponseDto deDominio(Transacao transacao) {
        return new TransacaoResponseDto(
                transacao.getId(),
                transacao.getContaOrigem() != null ? transacao.getContaOrigem().getNumeroConta() : null,
                transacao.getContaOrigem() != null ? transacao.getContaOrigem().getTitular() : null,
                transacao.getContaDestino() != null ? transacao.getContaDestino().getNumeroConta() : null,
                transacao.getContaDestino() != null ? transacao.getContaDestino().getTitular() : null,
                transacao.getValor(),
                transacao.getDataHora(),
                transacao.getStatus(),
                transacao.getDescricaoErro()
        );
    }
}
