package com.example.SistemaBiblioteca_api.repository;

import com.example.SistemaBiblioteca_api.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor,Long> {
}
