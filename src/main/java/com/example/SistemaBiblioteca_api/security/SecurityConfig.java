package com.example.SistemaBiblioteca_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    // diz ao spring para injetá-lo par o service
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
        // cria a implementação que gera e compara hashes com BCrypt
    }

    // metodo para deixar permitir as rotas do controller
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/cadastro", "/auth/login").permitAll()
                        .anyRequest().authenticated()
                        // exige autenticação das outras rotas
                );
        // finaliza essa configuração e cria o SecurityFilterChain
        return httpSecurity.build();
    }

    // metodo para verificar o e-mail e a senha enviados nessa rota
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}
