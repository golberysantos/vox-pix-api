package br.com.voxpixapi.pix.infrastructure.client.mock;

import br.com.voxpixapi.pix.domain.model.ComandoPix;
import br.com.voxpixapi.pix.domain.model.ComandoPix.Intencao;
import br.com.voxpixapi.pix.domain.service.ExtracaoComandoService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simulador do Servico de Extracao de Comandos por IA (Mock Service).
 *
 * Ativado apenas no perfil "dev". Processa o texto localmente via Regex
 * simulando o comportamento de interpretacao de linguagem natural da IA.
 *
 * @author Golbery Santos
 */
@Service
@Profile("dev")
public class MockExtracaoComandoService implements ExtracaoComandoService {

    @Override
    public ComandoPix extrair(String textoComando) {
        String texto = textoComando.toLowerCase();

        // 1. Caso de Intencao de Consulta de Saldo
        if (texto.contains("saldo") || texto.contains("quanto eu tenho")) {
            return new ComandoPix(Intencao.CONSULTA_SALDO, null, null, null);
        }

        // 2. Caso de Intencao de Transferencia / Pix
        if (texto.contains("transfira") || texto.contains("transferir") || texto.contains("pix")) {
            BigDecimal valor = extrairValor(texto);
            String destinatario = extrairDestinatario(texto);

            if (valor == null || destinatario == null) {
                return new ComandoPix(Intencao.ERRO, null, null, 
                        "Nao foi possivel extrair o valor ou o destinatario do comando.");
            }

            return new ComandoPix(Intencao.REALIZAR_TRANSFERENCIA, valor, destinatario, null);
        }

        // 3. Caso padrao de erro de interpretacao
        return new ComandoPix(Intencao.ERRO, null, null, 
                "Comando nao reconhecido. Diga algo como 'Saldo' ou 'Transfira X reais para Y'.");
    }

    private BigDecimal extrairValor(String texto) {
        // Encontra o primeiro numero inteiro ou decimal no texto
        Pattern pattern = Pattern.compile("(\\d+(?:[.,]\\d{2})?)");
        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            String valorStr = matcher.group(1).replace(",", ".");
            try {
                return new BigDecimal(valorStr);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String extrairDestinatario(String texto) {
        // Captura o nome que vem logo apos as preposicoes comuns
        if (texto.contains("para a ")) {
            return capturarNomeApos(texto, "para a ");
        } else if (texto.contains("para o ")) {
            return capturarNomeApos(texto, "para o ");
        } else if (texto.contains("para ")) {
            return capturarNomeApos(texto, "para ");
        }
        return null;
    }

    private String capturarNomeApos(String texto, String preposicao) {
        int index = texto.indexOf(preposicao) + preposicao.length();
        String resto = texto.substring(index).trim();
        String[] palavras = resto.split("\\s+");
        if (palavras.length > 0) {
            String nome = palavras[0];
            // Capitaliza o nome para padronizacao (ex: "maria" -> "Maria")
            return nome.substring(0, 1).toUpperCase() + nome.substring(1);
        }
        return null;
    }
}
