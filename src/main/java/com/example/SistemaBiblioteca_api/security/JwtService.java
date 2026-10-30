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
        // Jwts.builder() monta e gera um JWT
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

    // usar o token na api
    public String extrairEmailDoToken(String token){
       // Jwts.parser lê e valida um Jwt que já existe
        return Jwts.parser()
                // informar qual chave deve ser usada para conferir a assinatura
                .verifyWith(obterChaveAssinatura())
                // finaliza a configuração e cria um parser para fazer o uso
                .build()
                // o token e passado pelo .parseSignedClaims(token)
                .parseSignedClaims(token)
                // ver o conteudo do token
                .getPayload()
                // pega o valor do campo sub
                // sub e o indentificador de usuario
                .getSubject();
    }
}
