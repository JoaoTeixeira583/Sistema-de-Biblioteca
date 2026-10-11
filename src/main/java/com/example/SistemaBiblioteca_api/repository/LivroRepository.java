package com.example.SistemaBiblioteca_api.repository;

import com.example.SistemaBiblioteca_api.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro,Long> {

    boolean existsByAutorId(Long autorId);

}
