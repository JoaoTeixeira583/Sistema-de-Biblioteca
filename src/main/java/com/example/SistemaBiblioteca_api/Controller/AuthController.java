package com.example.SistemaBiblioteca_api.Controller;

import com.example.SistemaBiblioteca_api.dto.UsuarioCadastroDto;
import com.example.SistemaBiblioteca_api.dto.UsuarioResponseDto;
import com.example.SistemaBiblioteca_api.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/cadastro")
    //@RequestBody converte o corpo JSON em UsuarioCadastroDto.
    //@Valid aplica as validações como @Email, @NotBlank e @Size.
    public ResponseEntity<UsuarioResponseDto> cadastrarUsuario(@RequestBody @Valid UsuarioCadastroDto dto){
        UsuarioResponseDto usuarioCadastrado = usuarioService.cadastrarUsuario(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCadastrado);
    }
}

