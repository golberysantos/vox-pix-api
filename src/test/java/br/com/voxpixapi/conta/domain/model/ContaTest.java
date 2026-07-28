package br.com.voxpixapi.conta.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitarios para a entidade de dominio Conta.
 *
 * @author Golbery Santos
 */
class ContaTest {

    @Test
    @DisplayName("Deve creditar valor com sucesso na conta")
    void deveCreditarValorComSucesso() {
        // Arrange
        Conta conta = new Conta(1L, "1001-9", "Carlos Silva", "123.456.789-00", new BigDecimal("100.00"));

        // Act
        conta.creditar(new BigDecimal("50.00"));

        // Assert
        assertEquals(new BigDecimal("150.00"), conta.getSaldo());
    }

    @Test
    @DisplayName("Nao deve creditar valor menor ou igual a zero")
    void naoDeveCreditarValorInvalido() {
        Conta conta = new Conta(1L, "1001-9", "Carlos Silva", "123.456.789-00", new BigDecimal("100.00"));

        assertThrows(IllegalArgumentException.class, () -> conta.creditar(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> conta.creditar(new BigDecimal("-10.00")));
    }

    @Test
    @DisplayName("Deve debitar valor com sucesso da conta")
    void deveDebitarValorComSucesso() {
        Conta conta = new Conta(1L, "1001-9", "Carlos Silva", "123.456.789-00", new BigDecimal("100.00"));

        conta.debitar(new BigDecimal("30.00"));

        assertEquals(new BigDecimal("70.00"), conta.getSaldo());
    }

    @Test
    @DisplayName("Nao deve debitar valor se saldo for insuficiente")
    void naoDeveDebitarSemSaldoSuficiente() {
        Conta conta = new Conta(1L, "1001-9", "Carlos Silva", "123.456.789-00", new BigDecimal("100.00"));

        assertThrows(IllegalStateException.class, () -> conta.debitar(new BigDecimal("100.01")));
    }

    @Test
    @DisplayName("Nao deve debitar valor menor ou igual a zero")
    void naoDeveDebitarValorInvalido() {
        Conta conta = new Conta(1L, "1001-9", "Carlos Silva", "123.456.789-00", new BigDecimal("100.00"));

        assertThrows(IllegalArgumentException.class, () -> conta.debitar(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> conta.debitar(new BigDecimal("-5.00")));
    }
}
