package br.com.voxpixapi.conta.domain.repository;

import br.com.voxpixapi.conta.domain.model.Conta;
import java.util.Optional;

/**
 * Porta de Saída (Output Port) para operações de persistência de Contas.
 *
 * Sendo uma interface do domínio, ela define o contrato de banco de dados
 * sem acoplamento com tecnologias específicas como JPA ou Hibernate.
 *
 * @author Golbery Santos
 */
public interface ContaRepository {

    Optional<Conta> buscarPorNumero(String numeroConta);

    Optional<Conta> buscarPorCpf(String cpf);

    Optional<Conta> buscarPorId(Long id);

    Conta salvar(Conta conta);
}
