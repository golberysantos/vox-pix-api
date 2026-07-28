package br.com.voxpixapi.pix.domain.model;

import java.math.BigDecimal;

/**
 * Record de Dominio representando o Comando interpretado pela Inteligencia Artificial.
 *
 * Contem a intencao extraida, o valor (se houver), o destinatario (se houver)
 * e eventuais mensagens de erro de interpretacao.
 *
 * @author Golbery Santos
 */
public record ComandoPix(
        Intencao intencao,
        BigDecimal valor,
        String destinatario,
        String mensagemErro
) {
    public enum Intencao {
        CONSULTA_SALDO,
        REALIZAR_TRANSFERENCIA,
        ERRO
    }
}
