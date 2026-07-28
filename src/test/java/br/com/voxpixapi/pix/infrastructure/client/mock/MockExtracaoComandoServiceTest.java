package br.com.voxpixapi.pix.infrastructure.client.mock;

import br.com.voxpixapi.pix.domain.model.ComandoPix;
import br.com.voxpixapi.pix.domain.model.ComandoPix.Intencao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitarios para o processador local de comandos MockExtracaoComandoService.
 *
 * @author Golbery Santos
 */
class MockExtracaoComandoServiceTest {

    private final MockExtracaoComandoService service = new MockExtracaoComandoService();

    @Test
    @DisplayName("Deve interpretar comando de consulta de saldo com sucesso")
    void deveExtrairConsultaSaldo() {
        ComandoPix comando = service.extrair("Quero consultar meu saldo, por favor.");

        assertEquals(Intencao.CONSULTA_SALDO, comando.intencao());
        assertNull(comando.valor());
        assertNull(comando.destinatario());
        assertNull(comando.mensagemErro());
    }

    @Test
    @DisplayName("Deve interpretar comando de transferencia Pix simples com sucesso")
    void deveExtrairTransferenciaSimples() {
        ComandoPix comando = service.extrair("Transfira 150 reais para a Maria");

        assertEquals(Intencao.REALIZAR_TRANSFERENCIA, comando.intencao());
        assertTrue(new BigDecimal("150.00").compareTo(comando.valor()) == 0);
        assertEquals("Maria", comando.destinatario());
        assertNull(comando.mensagemErro());
    }

    @Test
    @DisplayName("Deve interpretar valor com centavos e preposicao masculina")
    void deveExtrairTransferenciaComCentavosEMasculino() {
        ComandoPix comando = service.extrair("Faca um Pix de 300,50 para o Joao");

        assertEquals(Intencao.REALIZAR_TRANSFERENCIA, comando.intencao());
        assertEquals(new BigDecimal("300.50"), comando.valor());
        assertEquals("Joao", comando.destinatario());
        assertNull(comando.mensagemErro());
    }

    @Test
    @DisplayName("Deve retornar intencao de erro caso o valor nao possa ser identificado")
    void deveRetornarErroParaComandoIncompleto() {
        ComandoPix comando = service.extrair("Transfira um valor para o Carlos");

        assertEquals(Intencao.ERRO, comando.intencao());
        assertNotNull(comando.mensagemErro());
    }
}
