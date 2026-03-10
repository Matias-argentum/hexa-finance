package com.mat.hexa_finance.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitamos CSRF para que HTMX pueda hacer POST sin tokens
                .csrf(csrf -> csrf.disable())

                // 2. Permitimos que cualquier petición entre sin autenticación
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // 3. Deshabilitamos el formulario de login por defecto
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
