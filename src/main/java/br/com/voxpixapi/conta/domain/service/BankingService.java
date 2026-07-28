package br.com.voxpixapi.conta.domain.service;

import br.com.voxpixapi.conta.domain.model.Conta;
import br.com.voxpixapi.conta.domain.model.Transacao;
import br.com.voxpixapi.conta.domain.model.Transacao.StatusTransacao;
import br.com.voxpixapi.conta.domain.repository.ContaRepository;
import br.com.voxpixapi.conta.domain.repository.TransacaoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servico de Dominio puro contendo as regras de negocio bancarias (Use Case).
 *
 * Realiza as validacoes de saldo, debitos, creditos e registros de transacao.
 *
 * @author Golbery Santos
 */
public class BankingService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    public BankingService(ContaRepository contaRepository, TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    /**
     * Busca uma conta ativa pelo CPF do titular.
     */
    public Optional<Conta> buscarPorCpf(String cpf) {
        return contaRepository.buscarPorCpf(cpf);
    }

    /**
     * Busca uma conta ativa pelo nome (ou correspondencia de nome) do titular.
     */
    public Optional<Conta> buscarPorTitular(String nome) {
        return contaRepository.buscarPorTitular(nome);
    }

    /**
     * Executa a transferencia de valores via Pix entre contas.
     *
     * @param cpfOrigem CPF do cliente autenticado que envia o Pix.
     * @param nomeDestinatario Nome (ou apelido) do recebedor do Pix.
     * @param valor O montante a ser transferido.
     * @return O log de Transacao consolidado (SUCESSO ou FALHA).
     */
    public Transacao realizarTransferencia(String cpfOrigem, String nomeDestinatario, BigDecimal valor) {
        // 1. Busca a conta de origem pelo CPF autenticado
        Optional<Conta> origemOpt = contaRepository.buscarPorCpf(cpfOrigem);
        if (origemOpt.isEmpty()) {
            throw new IllegalArgumentException("Conta de origem nao encontrada para o CPF autenticado.");
        }
        Conta origem = origemOpt.get();

        // 2. Busca a conta de destino por correspondencia de nome do recebedor
        Optional<Conta> destinoOpt = contaRepository.buscarPorTitular(nomeDestinatario);
        if (destinoOpt.isEmpty()) {
            Transacao falha = new Transacao(
                    null,
                    origem,
                    null,
                    valor,
                    LocalDateTime.now(),
                    StatusTransacao.FALHA,
                    "Conta de destino '" + nomeDestinatario + "' nao encontrada no banco."
            );
            return transacaoRepository.salvar(falha);
        }
        Conta destino = destinoOpt.get();

        // 3. Valida saldo suficiente
        if (origem.getSaldo().compareTo(valor) < 0) {
            Transacao falha = new Transacao(
                    null,
                    origem,
                    destino,
                    valor,
                    LocalDateTime.now(),
                    StatusTransacao.FALHA,
                    "Saldo insuficiente para realizar a transferencia de R$ " + valor
            );
            return transacaoRepository.salvar(falha);
        }

        // 4. Efetua a operacao de debito e credito (Imutabilidade de modelos do Dominio)
        Conta origemAtualizada = new Conta(
                origem.getId(),
                origem.getNumeroConta(),
                origem.getTitular(),
                origem.getCpf(),
                origem.getSaldo().subtract(valor)
        );

        Conta destinoAtualizado = new Conta(
                destino.getId(),
                destino.getNumeroConta(),
                destino.getTitular(),
                destino.getCpf(),
                destino.getSaldo().add(valor)
        );

        // 5. Persiste as alteracoes e registra a transacao de sucesso
        contaRepository.salvar(origemAtualizada);
        contaRepository.salvar(destinoAtualizado);

        Transacao sucesso = new Transacao(
                null,
                origemAtualizada,
                destinoAtualizado,
                valor,
                LocalDateTime.now(),
                StatusTransacao.SUCESSO,
                null
        );
        return transacaoRepository.salvar(sucesso);
    }
}
