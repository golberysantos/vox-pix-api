package br.com.voxpixapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração central de segurança da API (Spring Security).
 *
 * Define os filtros de autorização de rotas e gerencia os usuários em memória
 * para a simulação dos clientes bancários.
 *
 * @author Golbery Santos
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF por ser uma API REST Stateless
            .authorizeHttpRequests(auth -> auth
                // Rotas de console do banco H2 necessitam de liberação temporária em dev
                .requestMatchers("/h2-console/**").permitAll()
                // Qualquer outra requisição à API exige autenticação HTTP Basic
                .anyRequest().authenticated()
            )
            // Permite renderização de frames para o console H2 funcionar no navegador
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // Usuários correspondentes às contas salvas no banco
        UserDetails carlos = User.builder()
                .username("carlos")
                .password(passwordEncoder.encode("carlos123"))
                .roles("USER")
                .build();

        UserDetails maria = User.builder()
                .username("maria")
                .password(passwordEncoder.encode("maria123"))
                .roles("USER")
                .build();

        UserDetails joao = User.builder()
                .username("joao")
                .password(passwordEncoder.encode("joao123"))
                .roles("USER")
                .build();

        UserDetails visitante = User.builder()
                .username("visitante")
                .password(passwordEncoder.encode("visitante123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(carlos, maria, joao, visitante);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
