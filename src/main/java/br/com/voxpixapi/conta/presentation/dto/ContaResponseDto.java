package br.com.voxpixapi.conta.presentation.dto;

import br.com.voxpixapi.conta.domain.model.Conta;
import java.math.BigDecimal;

/**
 * DTO para expor os dados básicos de uma conta bancária sem expor IDs e CPFs.
 *
 * @author Golbery Santos
 */
public record ContaResponseDto(
        String numeroConta,
        String titular,
        BigDecimal saldo
) {
    public static ContaResponseDto deDominio(Conta conta) {
        return new ContaResponseDto(
                conta.getNumeroConta(),
                conta.getTitular(),
                conta.getSaldo()
        );
    }
}
