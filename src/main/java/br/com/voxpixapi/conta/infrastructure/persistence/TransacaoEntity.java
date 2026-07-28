package br.com.voxpixapi.conta.infrastructure.persistence;

import br.com.voxpixapi.conta.domain.model.Transacao;
import br.com.voxpixapi.conta.domain.model.Transacao.StatusTransacao;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade JPA representando a tabela tb_transacoes no banco de dados.
 *
 * @author Golbery Santos
 */
@Entity
@Table(name = "tb_transacoes")
public class TransacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_origem_id", nullable = false)
    private ContaEntity contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_destino_id")
    private ContaEntity contaDestino;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTransacao status;

    @Column(name = "descricao_erro")
    private String descricaoErro;

    public TransacaoEntity() {}

    public Transacao paraDominio() {
        return new Transacao(
                this.id,
                this.contaOrigem != null ? this.contaOrigem.paraDominio() : null,
                this.contaDestino != null ? this.contaDestino.paraDominio() : null,
                this.valor,
                this.dataHora,
                this.status,
                this.descricaoErro
        );
    }

    public static TransacaoEntity deDominio(Transacao transacao) {
        TransacaoEntity entity = new TransacaoEntity();
        entity.setId(transacao.getId());
        entity.setContaOrigem(transacao.getContaOrigem() != null ? ContaEntity.deDominio(transacao.getContaOrigem()) : null);
        entity.setContaDestino(transacao.getContaDestino() != null ? ContaEntity.deDominio(transacao.getContaDestino()) : null);
        entity.setValor(transacao.getValor());
        entity.setDataHora(transacao.getDataHora());
        entity.setStatus(transacao.getStatus());
        entity.setDescricaoErro(transacao.getDescricaoErro());
        return entity;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContaEntity getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(ContaEntity contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public ContaEntity getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(ContaEntity contaDestino) {
        this.contaDestino = contaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusTransacao getStatus() {
        return status;
    }

    public void setStatus(StatusTransacao status) {
        this.status = status;
    }

    public String getDescricaoErro() {
        return descricaoErro;
    }

    public void setDescricaoErro(String descricaoErro) {
        this.descricaoErro = descricaoErro;
    }
}
