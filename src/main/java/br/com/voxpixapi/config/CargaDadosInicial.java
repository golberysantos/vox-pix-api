package br.com.voxpixapi.config;

import br.com.voxpixapi.conta.domain.model.Conta;
import br.com.voxpixapi.conta.domain.repository.ContaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * Componente de Carga de Dados Inicial (Seeding) para fins de teste.
 *
 * Insere contas ficticias no banco H2 em memoria assim que a aplicacao
 * Spring Boot inicia com sucesso.
 *
 * @author Golbery Santos
 */
@Component
public class CargaDadosInicial implements CommandLineRunner {

    private final ContaRepository contaRepository;

    public CargaDadosInicial(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verifica se as contas ja existem no banco para evitar duplicidades
        if (contaRepository.buscarPorNumero("1001-9").isEmpty()) {
            System.out.println(">>> Populando banco de dados com contas ficticias de teste...");

            Conta carlos = new Conta(null, "1001-9", "Carlos Silva", "123.456.789-00", new BigDecimal("1000.00"));
            Conta maria = new Conta(null, "2002-8", "Maria Oliveira", "987.654.321-11", new BigDecimal("2500.00"));
            Conta joao = new Conta(null, "3003-7", "Joao Souza", "111.222.333-44", new BigDecimal("500.00"));

            contaRepository.salvar(carlos);
            contaRepository.salvar(maria);
            contaRepository.salvar(joao);

            System.out.println(">>> Carga inicial de contas realizada com sucesso!");
        }
    }
}
