package br.com.voxpixapi.conta.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Interface Spring Data JPA para operacoes do banco de dados na tabela de contas.
 *
 * @author Golbery Santos
 */
public interface SpringDataContaRepository extends JpaRepository<ContaEntity, Long> {

    Optional<ContaEntity> findByNumeroConta(String numeroConta);

    Optional<ContaEntity> findByCpf(String cpf);

    Optional<ContaEntity> findFirstByTitularContainingIgnoreCase(String titular);
}
