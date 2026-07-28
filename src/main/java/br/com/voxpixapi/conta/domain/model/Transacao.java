package br.com.voxpixapi.conta.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade de Dominio representando uma Transacao Bancaria (Pix).
 *
 * Mantida pura de dependencias do Hibernate ou Spring.
 *
 * @author Golbery Santos
 */
public class Transacao {

    private final Long id;
    private final Conta contaOrigem;
    private final Conta contaDestino;
    private final BigDecimal valor;
    private final LocalDateTime dataHora;
    private final StatusTransacao status;
    private final String descricaoErro;

    public Transacao(Long id, Conta contaOrigem, Conta contaDestino, BigDecimal valor, 
                     LocalDateTime dataHora, StatusTransacao status, String descricaoErro) {
        this.id = id;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.valor = valor;
        this.dataHora = dataHora;
        this.status = status;
        this.descricaoErro = descricaoErro;
    }

    public Long getId() {
        return id;
    }

    public Conta getContaOrigem() {
        return contaOrigem;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public StatusTransacao getStatus() {
        return status;
    }

    public String getDescricaoErro() {
        return descricaoErro;
    }

    public enum StatusTransacao {
        SUCESSO,
        FALHA
    }
}
