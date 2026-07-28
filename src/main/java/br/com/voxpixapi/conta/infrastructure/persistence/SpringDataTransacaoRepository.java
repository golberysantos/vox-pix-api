package br.com.voxpixapi.conta.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Interface Spring Data JPA para operacoes do banco de dados na tabela de transacoes.
 *
 * @author Golbery Santos
 */
public interface SpringDataTransacaoRepository extends JpaRepository<TransacaoEntity, Long> {

    List<TransacaoEntity> findByContaOrigemId(Long contaOrigemId);
}
