package com.diogo.auth_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita o CSRF (padrão para APIs REST que usam JWT)
                .csrf(csrf -> csrf.disable())

                // Configura quais rotas são públicas e quais são privadas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Libera tudo que começa com /auth (login, cadastro, etc)
                        .anyRequest().authenticated() // Exige token JWT para qualquer outra rota no futuro
                )

                // Diz ao Spring para não guardar sessão, pois usaremos o Token JWT a cada requisição
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}