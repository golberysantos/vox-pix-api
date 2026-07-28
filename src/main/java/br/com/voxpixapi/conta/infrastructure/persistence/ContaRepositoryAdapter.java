package br.com.voxpixapi.conta.infrastructure.persistence;

import br.com.voxpixapi.conta.domain.model.Conta;
import br.com.voxpixapi.conta.domain.repository.ContaRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * Adaptador de Persistencia para o modulo de Conta (Adapter Pattern).
 *
 * Esta classe implementa a interface de repositorio do dominio (Porta) e
 * delega as chamadas de banco para o Spring Data JPA, realizando as conversoes
 * de tipos.
 *
 * @author Golbery Santos
 */
@Component
public class ContaRepositoryAdapter implements ContaRepository {

    private final SpringDataContaRepository springDataRepository;

    public ContaRepositoryAdapter(SpringDataContaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Optional<Conta> buscarPorNumero(String numeroConta) {
        return springDataRepository.findByNumeroConta(numeroConta)
                .map(ContaEntity::paraDominio);
    }

    @Override
    public Optional<Conta> buscarPorCpf(String cpf) {
        return springDataRepository.findByCpf(cpf)
                .map(ContaEntity::paraDominio);
    }

    @Override
    public Optional<Conta> buscarPorId(Long id) {
        return springDataRepository.findById(id)
                .map(ContaEntity::paraDominio);
    }

    @Override
    public Conta salvar(Conta conta) {
        ContaEntity entity = ContaEntity.deDominio(conta);
        ContaEntity entitySalva = springDataRepository.save(entity);
        return entitySalva.paraDominio();
    }

    @Override
    public Optional<Conta> buscarPorTitular(String titular) {
        return springDataRepository.findFirstByTitularContainingIgnoreCase(titular)
                .map(ContaEntity::paraDominio);
    }

    @Override
    public java.util.List<Conta> listarTodas() {
        return springDataRepository.findAll().stream()
                .map(ContaEntity::paraDominio)
                .collect(java.util.stream.Collectors.toList());
    }
}
