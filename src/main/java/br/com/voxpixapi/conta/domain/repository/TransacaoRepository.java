package br.com.voxpixapi.conta.domain.repository;

import br.com.voxpixapi.conta.domain.model.Transacao;
import java.util.List;
import java.util.Optional;

/**
 * Porta de Saida (Output Port) para operacoes de persistencia de Transacoes.
 *
 * @author Golbery Santos
 */
public interface TransacaoRepository {

    Transacao salvar(Transacao transacao);

    Optional<Transacao> buscarPorId(Long id);

    List<Transacao> buscarPorContaOrigemId(Long contaOrigemId);
}
