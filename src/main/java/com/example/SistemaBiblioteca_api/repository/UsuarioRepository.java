package com.example.SistemaBiblioteca_api.repository;

import com.example.SistemaBiblioteca_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long>{
    // metodo para buscar email
    Optional<Usuario> findByEmailIgnoreCase(String email);

}
