package br.com.voxpixapi.conta.infrastructure.persistence;

import br.com.voxpixapi.conta.domain.model.Transacao;
import br.com.voxpixapi.conta.domain.repository.TransacaoRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de Persistencia para o modulo de Transacao (Adapter Pattern).
 *
 * @author Golbery Santos
 */
@Component
public class TransacaoRepositoryAdapter implements TransacaoRepository {

    private final SpringDataTransacaoRepository springDataRepository;

    public TransacaoRepositoryAdapter(SpringDataTransacaoRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Transacao salvar(Transacao transacao) {
        TransacaoEntity entity = TransacaoEntity.deDominio(transacao);
        TransacaoEntity entitySalva = springDataRepository.save(entity);
        return entitySalva.paraDominio();
    }

    @Override
    public Optional<Transacao> buscarPorId(Long id) {
        return springDataRepository.findById(id)
                .map(TransacaoEntity::paraDominio);
    }

    @Override
    public List<Transacao> buscarPorContaOrigemId(Long contaOrigemId) {
        return springDataRepository.findByContaOrigemId(contaOrigemId)
                .stream()
                .map(TransacaoEntity::paraDominio)
                .collect(Collectors.toList());
    }
}
