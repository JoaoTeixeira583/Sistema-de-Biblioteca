package com.example.SistemaBiblioteca_api.service;


import com.example.SistemaBiblioteca_api.dto.AutorResponseDTO;
import com.example.SistemaBiblioteca_api.entity.Autor;
import com.example.SistemaBiblioteca_api.mapper.AutorMapper;
import com.example.SistemaBiblioteca_api.repository.AutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

// classe teste serve para automatizar um metodo para não fazer manualmente
@ExtendWith(MockitoExtension.class)
public class AutorServiceTest {

    @InjectMocks
    // cria um AutorService real e coloca esses mocks dentro dele
    // são objetos proprios não são alterado
    private  AutorService autorService;

    @Mock
    // cria uma dependência simulada
    // Mock marca o repositorio e o mapper como simulados
    private  AutorRepository autorRepository;
    @Mock
    private  AutorMapper autorMapper;

    @Test
    // marac que sera um teste que o JUnit para executar
    // metodo de teste na maioria retorna um void
    public void testeNomeBuscaAutor(){
        String nome = "Clarice";
        Pageable pageable = PageRequest.of(0, 10);

        Autor autor = new Autor();
        autor.setId(3L);
        autor.setNome("Clarice Lispector");
        autor.setNacionalidade("Ucrania-Brasileira");

        AutorResponseDTO autorDTO = AutorResponseDTO.builder()
                .id(3L)
                .nome("Clarice Lispector")
                .nacionalidade("Ucrania-Brasileira")
                .livros(List.of())
                .build();

       // cria um pagina teste conteudo autor sem usar o banco de dados
        Page<Autor> paginaAutores = new PageImpl<>(List.of(autor),pageable,1);

        // usada para configurar o Mock
        when(autorRepository.findByNomeContainingIgnoreCase(nome, pageable))
                .thenReturn(paginaAutores);

        when(autorMapper.toDto(autor)).thenReturn(autorDTO);

        // para verificar o metodo
        Page<AutorResponseDTO> paginaAutorNomeTeste = autorService.buscarAutorNome(nome,pageable);

        assertEquals(1, paginaAutorNomeTeste.getNumberOfElements());
        assertEquals(
                "Clarice Lispector",
                paginaAutorNomeTeste.getContent().get(0).getNome()
        );
    }

    // metodo para buscar nome sem resultados
    @Test
    public void buscarNomeAutorSemResultados(){
        String nome = "Henrique";
        // uma pagina começa na pagina 0 vai ate  a 10
        Pageable pageable = PageRequest.of(0,10);

        Page<Autor> paginaAutores = new PageImpl<>(List.of(),pageable,0);

        when(autorRepository.findByNomeContainingIgnoreCase(nome,pageable))
                .thenReturn(paginaAutores);

        Page<AutorResponseDTO> paginaAutor = autorService.buscarAutorNome(nome,pageable);

        // para ver se o resultado estiver vazio
        assertTrue(paginaAutor.isEmpty());
    }

    @Test
    public void buscarAutorIdSemResultados(){
        Long id = 99L;

        when(autorRepository.findById(id))
                .thenReturn(Optional.empty());

        //verifica se uma chamada lança uma exceção
        assertThrows(
                EntityNotFoundException.class,
                () -> autorService.buscarAutorId(id)
        );
    }
}
