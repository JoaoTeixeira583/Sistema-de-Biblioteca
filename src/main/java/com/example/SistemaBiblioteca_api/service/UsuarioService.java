package com.example.SistemaBiblioteca_api.service;



import com.example.SistemaBiblioteca_api.dto.AuthResponseDto;
import com.example.SistemaBiblioteca_api.dto.UsuarioCadastroDto;
import com.example.SistemaBiblioteca_api.dto.UsuarioLoginDto;
import com.example.SistemaBiblioteca_api.dto.UsuarioResponseDto;
import com.example.SistemaBiblioteca_api.entity.Usuario;
import com.example.SistemaBiblioteca_api.repository.UsuarioRepository;
import com.example.SistemaBiblioteca_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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

    public AuthResponseDto login(UsuarioLoginDto dto){
         // reune o e-mail e a senha
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                dto.getEmail(),
                dto.getSenha()
        );

        // valida as credenciais. Se forem inválidas, ele lança uma exceção e o código não segue para a geração do JWT.
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Se a autenticação passar, você pega o UserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //  gera o token e devolve o AuthResponseDto.
        String token = jwtService.gerarToken(userDetails);

        // coloque o token gerado no AuthResponseDto
        AuthResponseDto authResponseDto = AuthResponseDto.builder()
                .token(token)
                .build();

        return authResponseDto;
    }

}
