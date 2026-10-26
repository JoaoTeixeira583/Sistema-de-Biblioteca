package com.example.SistemaBiblioteca_api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

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

    // metodo para gerar um token
    public String gerarToken(UserDetails userDetails){
        // obter o instante atual
        Instant agora = Instant.now();

        // definir a expiração do token
        Instant expiracao = agora.plusSeconds(15 * 60);

        // iniciar montagem de tokem
        return Jwts.builder()
                //   definir o e-mail como subject
                .setSubject(userDetails.getUsername())
                //  definir a data de emissão
                .setIssuedAt(Date.from(agora))
                //  definir a data de expiração
                .setExpiration(Date.from(expiracao))
                // assinar com a chave do JwtService
                .signWith(obterChaveAssinatura(), SignatureAlgorithm.HS256)
                // converter para o formato compacto
                .compact();
    }
}
