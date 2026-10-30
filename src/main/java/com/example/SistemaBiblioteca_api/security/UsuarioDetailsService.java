package com.example.SistemaBiblioteca_api.security;

import com.example.SistemaBiblioteca_api.entity.Usuario;
import com.example.SistemaBiblioteca_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    //para buscar o email do login para ver se existe
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuarioEncontrado = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return User.withUsername(usuarioEncontrado.getEmail())
                .password(usuarioEncontrado.getSenha())
                .authorities("ROLE_USER")
                .build();
    }
}
