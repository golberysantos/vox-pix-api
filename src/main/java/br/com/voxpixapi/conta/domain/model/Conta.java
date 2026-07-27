package br.com.voxpixapi.conta.domain.model;

import java.math.BigDecimal;

/**
 * Modelo de domínio puro que representa uma Conta Bancária.
 *
 * Esta classe é livre de anotações de frameworks (como JPA ou Spring)
 * para manter o domínio desacoplado dos detalhes de infraestrutura.
 *
 * @author Golbery Santos
 */
public class Conta {

    private final Long id;
    private final String numeroConta;
    private final String titular;
    private final String cpf;
    private BigDecimal saldo;

    public Conta(Long id, String numeroConta, String titular, String cpf, BigDecimal saldo) {
        this.id = id;
        this.numeroConta = numeroConta;
        this.titular = titular;
        this.cpf = cpf;
        this.saldo = saldo;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public String getTitular() {
        return titular;
    }

    public String getCpf() {
        return cpf;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    /**
     * Realiza um depósito (crédito) na conta.
     *
     * @param valor Valor monetário a ser depositado.
     * @throws IllegalArgumentException se o valor for nulo ou menor/igual a zero.
     */
    public void creditar(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do deposito deve ser maior que zero.");
        }
        this.saldo = this.saldo.add(valor);
    }

    /**
     * Realiza um saque ou débito na conta.
     *
     * @param valor Valor monetário a ser debitado.
     * @throws IllegalArgumentException se o valor for nulo ou menor/igual a zero.
     * @throws IllegalStateException se o saldo atual for insuficiente.
     */
    public void debitar(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do debito deve ser maior que zero.");
        }
        if (this.saldo.compareTo(valor) < 0) {
            throw new IllegalStateException("Saldo insuficiente para realizar o debito.");
        }
        this.saldo = this.saldo.subtract(valor);
    }
}
