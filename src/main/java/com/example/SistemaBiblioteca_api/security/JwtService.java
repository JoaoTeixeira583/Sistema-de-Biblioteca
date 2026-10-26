package com.example.SistemaBiblioteca_api.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    // para pegar o jwt.secret na configuração da aplicação e colocar o valor no secret
    // para assinar os tokens
    @Value("${jwt.secret}")
    private String secret;

    // decodifica o Base64 e cria uma SecretKey para assinar o token.
    private SecretKey obterChaveAssinatura(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
