package com.example.SistemaBiblioteca_api.service;


import com.example.SistemaBiblioteca_api.dto.AuthResponseDto;
import com.example.SistemaBiblioteca_api.dto.UsuarioCadastroDto;
import com.example.SistemaBiblioteca_api.dto.UsuarioLoginDto;
import com.example.SistemaBiblioteca_api.dto.UsuarioResponseDto;
import com.example.SistemaBiblioteca_api.entity.Usuario;
import com.example.SistemaBiblioteca_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDto cadastrarUsuario(UsuarioCadastroDto dto){
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmailIgnoreCase(dto.getEmail());

        // verificar email duplicado
        if(usuarioExistente.isPresent()){
            // para mandar uma mensagem httpStatus
               throw new ResponseStatusException(
                       HttpStatus.CONFLICT,
                       "Já existe um usuário cadastrado com esse email"
               );
        }

        // gerar o hash da senha
        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        // salvar usuario
        Usuario usuarioCriado = usuarioRepository.save(usuario);

        // devolver a reposta com id e amil para o cliente
        UsuarioResponseDto usuarioResponseDto = UsuarioResponseDto.builder()
                .id(usuarioCriado.getId())
                .email(usuarioCriado.getEmail())
                .build();

        return usuarioResponseDto;
    }

}
