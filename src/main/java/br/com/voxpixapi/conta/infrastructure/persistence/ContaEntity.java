package br.com.voxpixapi.conta.infrastructure.persistence;

import br.com.voxpixapi.conta.domain.model.Conta;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidade de Banco de Dados representando a tabela 'tb_contas'.
 *
 * Esta classe contem as anotacoes JPA para o Hibernate mapear a tabela
 * fisica e realizar as operacoes de persistencia.
 *
 * @author Golbery Santos
 */
@Entity
@Table(name = "tb_contas")
public class ContaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_conta", unique = true, nullable = false)
    private String numeroConta;

    @Column(nullable = false)
    private String titular;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private BigDecimal saldo;

    public ContaEntity() {}

    public ContaEntity(Long id, String numeroConta, String titular, String cpf, BigDecimal saldo) {
        this.id = id;
        this.numeroConta = numeroConta;
        this.titular = titular;
        this.cpf = cpf;
        this.saldo = saldo;
    }

    /**
     * Factory method para construir uma entidade a partir de um objeto de Dominio.
     */
    public static ContaEntity deDominio(Conta conta) {
        return new ContaEntity(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getTitular(),
                conta.getCpf(),
                conta.getSaldo()
        );
    }

    /**
     * Converte esta entidade de banco para o objeto puro de Dominio.
     */
    public Conta paraDominio() {
        return new Conta(id, numeroConta, titular, cpf, saldo);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
