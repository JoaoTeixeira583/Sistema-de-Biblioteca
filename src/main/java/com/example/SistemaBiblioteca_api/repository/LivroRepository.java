package com.example.SistemaBiblioteca_api.repository;

import com.example.SistemaBiblioteca_api.entity.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;




public interface LivroRepository extends JpaRepository<Livro,Long> {

    // metodo para ver se o autor existe
    boolean existsByAutorId(Long autorId);

   // metodo para buscar nome do livro (fazer uma comparação)
    // pageable mostra para o spring dataJpa para mudar a pagina quando achar o nome do livro
    Page<Livro> findByNomeContainingIgnoreCase (String nome, Pageable pageable);

    // para paginar os livros do autor especifico
    Page<Livro> findByAutorId(Long autorId,Pageable pageable);

    Page<Livro> findByNomeContainingIgnoreCaseAndAutorId(String nome,Long autorId,Pageable pageable);

    Long id(Long id);
}
