package br.com.voxpixapi.pix.presentation.controller;

import br.com.voxpixapi.pix.domain.model.ProcessamentoVozResult;
import br.com.voxpixapi.pix.domain.service.VoiceCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/**
 * Controlador REST para recebimento e processamento de comandos de voz (HTTP POST).
 *
 * @author Golbery Santos
 */
@RestController
@RequestMapping("/api/voz")
public class VoiceController {

    private final VoiceCommandService voiceCommandService;

    // Mapa estatico para identificar o CPF a partir do usuario logado (Simulacao de Single Sign-On)
    private static final Map<String, String> USUARIO_PARA_CPF = Map.of(
            "carlos", "123.456.789-00",
            "maria", "987.654.321-11",
            "joao", "111.222.333-44"
    );

    public VoiceController(VoiceCommandService voiceCommandService) {
        this.voiceCommandService = voiceCommandService;
    }

    /**
     * Recebe um audio gravado (.wav, .mp3, etc.) do dispositivo do usuario, transcreve e executa
     * o comando correspondente (Consulta de Saldo ou Transferencia Pix).
     *
     * @param userDetails Detalhes do usuario autenticado pelo Spring Security.
     * @param file O arquivo de audio gravado.
     * @return O resultado do processamento da voz.
     */
    @PostMapping(value = "/processar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProcessamentoVozResult> processarVoz(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file) {

        String username = userDetails.getUsername();
        String cpf = USUARIO_PARA_CPF.get(username);

        // Se o usuario autenticado nao possui CPF cadastrado no core banking, nega o acesso
        if (cpf == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    ProcessamentoVozResult.criarErro("", "Arquivo de audio invalido ou ausente.")
            );
        }

        // Executa a orquestracao de voz no Dominio
        ProcessamentoVozResult resultado = voiceCommandService.processarComandoVoz(cpf, file);

        if (!resultado.sucesso()) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }
}
