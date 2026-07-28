package br.com.voxpixapi.config;

import br.com.voxpixapi.conta.domain.repository.ContaRepository;
import br.com.voxpixapi.conta.domain.repository.TransacaoRepository;
import br.com.voxpixapi.conta.domain.service.BankingService;
import br.com.voxpixapi.pix.domain.service.ExtracaoComandoService;
import br.com.voxpixapi.pix.domain.service.TranscricaoVozService;
import br.com.voxpixapi.pix.domain.service.VoiceCommandService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuracao para instanciacao de Beans de dominio (Clean Architecture).
 *
 * Mapeia os servicos de dominio para Beans do Spring sem acoplar anotações
 * do framework nas classes de dominio puro.
 *
 * @author Golbery Santos
 */
@Configuration
public class PixServiceConfig {

    @Bean
    public BankingService bankingService(
            ContaRepository contaRepository, 
            TransacaoRepository transacaoRepository) {
        return new BankingService(contaRepository, transacaoRepository);
    }

    @Bean
    public VoiceCommandService voiceCommandService(
            TranscricaoVozService transcricaoVozService,
            ExtracaoComandoService extracaoComandoService,
            BankingService bankingService) {
        return new VoiceCommandService(transcricaoVozService, extracaoComandoService, bankingService);
    }
}
