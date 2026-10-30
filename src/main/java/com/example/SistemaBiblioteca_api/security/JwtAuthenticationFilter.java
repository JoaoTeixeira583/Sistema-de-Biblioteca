package com.example.SistemaBiblioteca_api.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// OncePerRequestFilter é uma classe base do Spring para criar filtros que devem processar a requisição uma vez durante o fluxo da requisição.
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // para buscar o cabeçalho chamado Authorization
        String autorizacao = request.getHeader("Authorization");

        // verificar o texto começa com outro texto
        if(autorizacao == null || !autorizacao.startsWith("Bearer ")){
            // entrega a requisição ao próximo filtro
           filterChain.doFilter(request,response);
           return;
        }
        // pega o texto a partir do oitavo caractere
        String token = autorizacao.substring(7);

        UserDetails userDetails = null;

        try {
            // validar a assinatura e a expiração do JWT antes de devolver o subject
            String email = jwtService.extrairEmailDoToken(token);

            // para carregar os usuarios
            userDetails = userDetailsService.loadUserByUsername(email);

        } catch (UsernameNotFoundException| JwtException e) {
             response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Token invalido ou expirado");
             return;
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, // principal
                null, // credenciais vazias
                userDetails.getAuthorities() // permissões
        );

        // para o spring security a reconehcer o usuário como autenticado
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        // passa a requisição  para o proximo filtro da cadeia Spring Security
        filterChain.doFilter(request,response);
    }
}
