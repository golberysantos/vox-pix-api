package br.com.voxpixapi.conta.presentation.controller;

import br.com.voxpixapi.conta.domain.model.Conta;
import br.com.voxpixapi.conta.domain.repository.ContaRepository;
import br.com.voxpixapi.conta.presentation.dto.ContaResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;
import java.util.Map;

/**
 * Controlador REST para operacoes relacionadas a Contas Bancarias.
 *
 * @author Golbery Santos
 */
@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaRepository contaRepository;

    // Mapeamento em memoria ligando o Username de autenticacao ao CPF registrado na Carga de Dados
    private static final Map<String, String> USUARIO_PARA_CPF = Map.of(
            "carlos", "123.456.789-00",
            "maria", "987.654.321-11",
            "joao", "111.222.333-44"
    );

    public ContaController(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    /**
     * Retorna as informacoes da conta do cliente que efetuou o login na requisicao HTTP.
     *
     * @param principal Objeto injetado pelo Spring Security contendo os dados do usuario logado.
     * @return DTO com os dados formatados da conta.
     */
    @GetMapping("/minha")
    public ContaResponseDto obterMinhaConta(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao autenticado.");
        }

        String username = principal.getName().toLowerCase();
        String cpf = USUARIO_PARA_CPF.get(username);

        if (cpf == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario nao associado a nenhuma conta.");
        }

        Conta conta = contaRepository.buscarPorCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta bancaria nao encontrada."));

        return ContaResponseDto.deDominio(conta);
    }
}
