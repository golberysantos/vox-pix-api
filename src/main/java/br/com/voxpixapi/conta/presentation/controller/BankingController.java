package br.com.voxpixapi.conta.presentation.controller;

import br.com.voxpixapi.conta.domain.model.Conta;
import br.com.voxpixapi.conta.domain.repository.ContaRepository;
import br.com.voxpixapi.conta.domain.repository.TransacaoRepository;
import br.com.voxpixapi.conta.presentation.dto.ContaResponseDto;
import br.com.voxpixapi.conta.presentation.dto.TransacaoResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST para administracao e consulta de dados de contas e transacoes.
 *
 * @author Golbery Santos
 */
@RestController
@RequestMapping("/api/contas")
public class BankingController {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    private static final Map<String, String> USUARIO_PARA_CPF = Map.of(
            "carlos", "123.456.789-00",
            "maria", "987.654.321-11",
            "joao", "111.222.333-44"
    );

    public BankingController(ContaRepository contaRepository, TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    /**
     * Retorna a lista de todas as contas cadastradas.
     * Util para demonstracao e visualizacao local dos saldos mudando em tempo real.
     *
     * @return Lista com os dados basicos das contas.
     */
    @GetMapping
    public ResponseEntity<List<ContaResponseDto>> listarTodasContas() {
        List<ContaResponseDto> contas = contaRepository.listarTodas().stream()
                .map(ContaResponseDto::deDominio)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contas);
    }

    /**
     * Retorna o historico de transacoes realizadas pelo usuario autenticado.
     *
     * @param userDetails Detalhes do usuario autenticado.
     * @return Lista com o historico de transacoes.
     */
    @GetMapping("/minha/transacoes")
    public ResponseEntity<List<TransacaoResponseDto>> obterMinhasTransacoes(
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        String cpf = USUARIO_PARA_CPF.get(username);

        if (cpf == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Conta> contaOpt = contaRepository.buscarPorCpf(cpf);
        if (contaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<TransacaoResponseDto> transacoes = transacaoRepository.buscarPorContaOrigemId(contaOpt.get().getId())
                .stream()
                .map(TransacaoResponseDto::deDominio)
                .collect(Collectors.toList());

        return ResponseEntity.ok(transacoes);
    }
}
