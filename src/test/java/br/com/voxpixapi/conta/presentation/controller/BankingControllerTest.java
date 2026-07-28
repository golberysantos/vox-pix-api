package br.com.voxpixapi.conta.presentation.controller;

import br.com.voxpixapi.config.SecurityConfig;
import br.com.voxpixapi.conta.domain.model.Conta;
import br.com.voxpixapi.conta.domain.repository.ContaRepository;
import br.com.voxpixapi.conta.domain.repository.TransacaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integracao e seguranca para o BankingController.
 *
 * Utiliza o padrao AAA (Arrange, Act, Assert).
 *
 * @author Golbery Santos
 */
@WebMvcTest(BankingController.class)
@Import(SecurityConfig.class)
class BankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContaRepository contaRepository;

    @MockitoBean
    private TransacaoRepository transacaoRepository;

    private String obterHeaderBasicAuth(String usuario, String senha) {
        String credenciais = usuario + ":" + senha;
        return "Basic " + Base64.getEncoder().encodeToString(credenciais.getBytes());
    }

    @Test
    @DisplayName("Deve retornar 401 se nao autenticado ao tentar listar contas")
    void deveRetornar401SeNaoAutenticadoAoListar() throws Exception {
        mockMvc.perform(get("/api/contas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve listar todas as contas com sucesso para usuario autenticado")
    void deveListarTodasAsContas() throws Exception {
        // Arrange
        Conta contaCarlos = new Conta(1L, "1001-9", "Carlos Silva", "123.456.789-00", new BigDecimal("1000.00"));
        Mockito.when(contaRepository.listarTodas()).thenReturn(List.of(contaCarlos));

        // Act & Assert
        mockMvc.perform(get("/api/contas")
                        .header("Authorization", obterHeaderBasicAuth("carlos", "carlos123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titular").value("Carlos Silva"))
                .andExpect(jsonPath("$[0].numeroConta").value("1001-9"));
    }

    @Test
    @DisplayName("Deve retornar historico de transacoes vazio se nenhuma transacao foi feita")
    void deveRetornarHistoricoVazio() throws Exception {
        // Arrange
        Conta contaCarlos = new Conta(1L, "1001-9", "Carlos Silva", "123.456.789-00", new BigDecimal("1000.00"));
        Mockito.when(contaRepository.buscarPorCpf("123.456.789-00")).thenReturn(Optional.of(contaCarlos));
        Mockito.when(transacaoRepository.buscarPorContaOrigemId(1L)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/contas/minha/transacoes")
                        .header("Authorization", obterHeaderBasicAuth("carlos", "carlos123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
