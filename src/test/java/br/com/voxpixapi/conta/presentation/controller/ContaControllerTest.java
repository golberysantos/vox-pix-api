package br.com.voxpixapi.conta.presentation.controller;

import br.com.voxpixapi.config.SecurityConfig;
import br.com.voxpixapi.conta.domain.model.Conta;
import br.com.voxpixapi.conta.domain.repository.ContaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integracao e seguranca da camada de apresentacao para ContaController.
 *
 * Utiliza o MockMvc para simular as requisicoes HTTP e validar os filtros de
 * seguranca do Spring Security.
 *
 * @author Golbery Santos
 */
@WebMvcTest(ContaController.class)
@Import(SecurityConfig.class)
class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContaRepository contaRepository;

    private String obterHeaderBasicAuth(String usuario, String senha) {
        String credenciais = usuario + ":" + senha;
        return "Basic " + Base64.getEncoder().encodeToString(credenciais.getBytes());
    }

    @Test
    @DisplayName("Deve barrar requisicao e retornar 401 Unauthorized se cliente nao fornecer credenciais")
    void deveRetornar401SeNaoAutenticado() throws Exception {
        mockMvc.perform(get("/api/contas/minha"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve autorizar e retornar os dados da conta quando autenticado como carlos")
    void deveRetornarDadosDaContaParaUsuarioAutenticado() throws Exception {
        Conta contaCarlos = new Conta(1L, "1001-9", "Carlos Silva", "123.456.789-00", new BigDecimal("1000.00"));
        Mockito.when(contaRepository.buscarPorCpf("123.456.789-00"))
                .thenReturn(Optional.of(contaCarlos));

        mockMvc.perform(get("/api/contas/minha")
                        .header("Authorization", obterHeaderBasicAuth("carlos", "carlos123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroConta").value("1001-9"))
                .andExpect(jsonPath("$.titular").value("Carlos Silva"))
                .andExpect(jsonPath("$.saldo").value(1000.00));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden se usuario autenticado nao estiver associado a nenhum CPF do banco")
    void deveRetornar403ParaUsuarioSemCpfMapeado() throws Exception {
        mockMvc.perform(get("/api/contas/minha")
                        .header("Authorization", obterHeaderBasicAuth("visitante", "visitante123")))
                .andExpect(status().isForbidden());
    }
}
