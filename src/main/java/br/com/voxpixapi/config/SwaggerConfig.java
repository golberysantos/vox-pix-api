package br.com.voxpixapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger / OpenAPI para documentação interativa da API.
 *
 * Registra o esquema de segurança HTTP Basic Auth globalmente no Swagger UI.
 *
 * @author Golbery Santos
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vox Pix API")
                        .version("1.0.0")
                        .description("API REST segura com orquestração de comandos por voz para operações Pix e consultas."))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(new Components()
                        .addSecuritySchemes("basicAuth",
                                new SecurityScheme()
                                        .name("basicAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                                        .description("Insira seu usuário e senha bancários (Ex: carlos / carlos123).")
                        )
                );
    }
}
