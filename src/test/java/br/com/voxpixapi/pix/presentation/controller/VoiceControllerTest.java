package br.com.voxpixapi.pix.presentation.controller;

import br.com.voxpixapi.config.PixServiceConfig;
import br.com.voxpixapi.config.SecurityConfig;
import br.com.voxpixapi.conta.domain.model.Conta;
import br.com.voxpixapi.conta.domain.repository.ContaRepository;
import br.com.voxpixapi.conta.domain.repository.TransacaoRepository;
import br.com.voxpixapi.pix.domain.model.ComandoPix;
import br.com.voxpixapi.pix.domain.model.ComandoPix.Intencao;
import br.com.voxpixapi.pix.domain.service.ExtracaoComandoService;
import br.com.voxpixapi.pix.domain.service.TranscricaoVozService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integracao e segurança da camada de apresentacao para VoiceController.
 *
 * Segue estritamente o padrao AAA (Arrange, Act, Assert).
 *
 * @author Golbery Santos
 */
@WebMvcTest(VoiceController.class)
@Import({SecurityConfig.class, PixServiceConfig.class})
class VoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContaRepository contaRepository;

    @MockitoBean
    private TransacaoRepository transacaoRepository;

    @MockitoBean
    private TranscricaoVozService transcricaoVozService;

    @MockitoBean
    private ExtracaoComandoService extracaoComandoService;

    private String obterHeaderBasicAuth(String usuario, String senha) {
        String credenciais = usuario + ":" + senha;
        return "Basic " + Base64.getEncoder().encodeToString(credenciais.getBytes());
    }

    @Test
    @DisplayName("Deve barrar requisicao se nao autenticado (retornando 401)")
    void deveRetornar401SeNaoAutenticado() throws Exception {
        // Arrange
        MockMultipartFile arquivo = new MockMultipartFile("file", "audio.mp3", "multipart/form-data", "conteudo".getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/api/voz/processar").file(arquivo))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve processar consulta de saldo com sucesso")
    void deveProcessarConsultaSaldoComSucesso() throws Exception {
        // Arrange
        String usuario = "carlos";
        String senha = "carlos123";
        String cpf = "123.456.789-00";
        MockMultipartFile arquivo = new MockMultipartFile("file", "saldo.mp3", "multipart/form-data", "conteudo".getBytes());
        Conta contaCarlos = new Conta(1L, "1001-9", "Carlos Silva", cpf, new BigDecimal("1000.00"));

        Mockito.when(transcricaoVozService.transcrever(Mockito.any()))
                .thenReturn("Quanto eu tenho de saldo?");
        Mockito.when(extracaoComandoService.extrair("Quanto eu tenho de saldo?"))
                .thenReturn(new ComandoPix(Intencao.CONSULTA_SALDO, null, null, null));
        Mockito.when(contaRepository.buscarPorCpf(cpf))
                .thenReturn(Optional.of(contaCarlos));

        // Act & Assert
        mockMvc.perform(multipart("/api/voz/processar")
                        .file(arquivo)
                        .header("Authorization", obterHeaderBasicAuth(usuario, senha)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transcricao").value("Quanto eu tenho de saldo?"))
                .andExpect(jsonPath("$.intencao").value("CONSULTA_SALDO"))
                .andExpect(jsonPath("$.mensagemRetorno").value("Seu saldo atual e de R$ 1000.00"))
                .andExpect(jsonPath("$.sucesso").value(true));
    }
}
