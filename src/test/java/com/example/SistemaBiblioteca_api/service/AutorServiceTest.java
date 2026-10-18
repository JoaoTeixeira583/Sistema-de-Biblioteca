package com.example.SistemaBiblioteca_api.service;


import com.example.SistemaBiblioteca_api.dto.AutorRequestDTO;
import com.example.SistemaBiblioteca_api.dto.AutorResponseDTO;
import com.example.SistemaBiblioteca_api.entity.Autor;
import com.example.SistemaBiblioteca_api.mapper.AutorMapper;
import com.example.SistemaBiblioteca_api.repository.AutorRepository;
import com.example.SistemaBiblioteca_api.repository.LivroRepository;
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
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.ls.LSInput;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
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
    private LivroRepository livroRepository;

    @Mock
    private  AutorMapper autorMapper;

    @Test
    // marac que sera um teste que o JUnit para executar
    // metodo de teste na maioria retorna um void
    public void buscarAutorNome_quandoEncontraAutor_retornaPaginaComAutor(){
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
    public void buscarAutorNome_quandoNaoEncontraAutor_retornaPaginaVazia(){
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
    public void buscarAutorId_quandoAutorNaoExiste_lancaEntityNotFoundException(){
        Long id = 99L;

        when(autorRepository.findById(id))
                .thenReturn(Optional.empty());

        //verifica se uma chamada lança uma exceção
        assertThrows(
                EntityNotFoundException.class,
                () -> autorService.buscarAutorId(id)
        );
    }

    @Test
    public void buscarAutorId_quandoAutorExiste_retornaDto(){
        Long id = 2L;

        Autor autor = new Autor();
        autor.setId(2L);
        autor.setNome("Joaquim Maria Machado de Assis");
        autor.setNacionalidade("Brasileira");

        AutorResponseDTO autorResponseDTO = AutorResponseDTO.builder()
                .id(2L)
                .nome("Joaquim Maria Machado de Assis")
                .nacionalidade("Brasileira")
                .build();

        when(autorRepository.findById(id))
                .thenReturn(Optional.of(autor));

        when(autorMapper.toDto(autor)).thenReturn(autorResponseDTO);

        AutorResponseDTO autorId = autorService.buscarAutorId(id);

        assertEquals(2L,autorId.getId());
        assertEquals(
                "Joaquim Maria Machado de Assis",
                 autorId.getNome()
        );

    }

    @Test
    public void atualizarAutor_quandoAutorNaoExiste_lancaEntityNotFoundException(){
        Long id = 99L;

        AutorRequestDTO autorRequestDTO = AutorRequestDTO.builder()
                .nome("Pedro")
                .nacionalidade("Africano")
                .build();


        when(autorRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                 EntityNotFoundException.class,
                 ()-> autorService.atualizarAutor(id,autorRequestDTO)
         );
    }

    @Test
    public void atualizarAutor_quandoAutorExiste_retornaDtoAtualizado(){
        Long id = 3L;

        Autor autor = new Autor();

        autor.setId(3L);
        autor.setNome("Clarice ");
        autor.setNacionalidade("Ucraniana-Brasileira");
        autor.setLivros(List.of());

        AutorRequestDTO autorRequestDTO = AutorRequestDTO.builder()
                .nome("Henrique Caio")
                .nacionalidade("Argentino")
                .build();

        AutorResponseDTO autorResponseDTO = AutorResponseDTO.builder()
                .id(3L)
                .nome("Henrique Caio")
                .nacionalidade("Argentino")
                .build();

        when(autorRepository.findById(id))
                .thenReturn(Optional.of(autor));

        when(autorRepository.save(autor))
                .thenReturn(autor);

        when(autorMapper.toDto(autor)).thenReturn(autorResponseDTO);

        AutorResponseDTO autorId = autorService.atualizarAutor(id,autorRequestDTO);

        // essa parte e para verificar se tudo esta cero como esperado
        assertEquals(3L,autorId.getId());

        assertEquals(
                "Henrique Caio",
                autorId.getNome()
        );

        assertEquals(
                "Henrique Caio",
                autor.getNome()
        );

        // assertEquals(valorEsperado,valorReal)

        assertEquals(
                "Argentino",
                autorId.getNacionalidade()
        );

        assertEquals(
                "Argentino",
                autor.getNacionalidade()
        );
    }

    @Test
    public void deletarAutor_quandoAutorPossuiLivros_lancaConflict(){
        Long id = 3L;

        when(livroRepository.existsByAutorId(id))
                .thenReturn(true);

        when(autorRepository.existsById(id))
                .thenReturn(true);

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> autorService.deletarAutor(id)
        );

        // verificar se o erro e 409
        assertEquals(409,erro.getStatusCode().value());
    }

    @Test
    public void deletarAutor_quandoAutorNaoPossuiLivros_removeAutor(){
        Long id = 3L;

        when(autorRepository.existsById(id))
                .thenReturn(true);

        when(livroRepository.existsByAutorId(id))
                .thenReturn(false);

        autorService.deletarAutor(id);

        // confirmar que uma chamada aconteceu em um mock
        verify(autorRepository).deleteById(id);
    }

    @Test
    public void deletarAutor_quandoAutorNaoExiste_lancaEntityNotFoundException(){
        Long id = 3L;

        when(autorRepository.existsById(id))
                .thenReturn(false);

        assertThrows(
                EntityNotFoundException.class,
                ()-> autorService.deletarAutor(id)
        );
    }

    @Test
    public void salvarAutor_quandoDadosValidos_retornaDtoDoAutorSalvo(){

        AutorRequestDTO autorRequestDTO = new AutorRequestDTO();

        autorRequestDTO.setNome("João Pedro");
        autorRequestDTO.setNacionalidade("Brasileiro");

        Autor autor = new Autor();

        autor.setId(5L);
        autor.setNome("João Pedro");
        autor.setNacionalidade("Brasileiro");
        autor.setLivros(List.of());

        AutorResponseDTO autorResponseDTO = new AutorResponseDTO();

        autorResponseDTO.setId(5L);
        autorResponseDTO.setNome("João Pedro");
        autorResponseDTO.setNacionalidade("Brasileiro");
        autorResponseDTO.setLivros(List.of());

        when(autorRepository.save(autor))
                .thenReturn(autor);

        when(autorMapper.toEntity(autorRequestDTO))
                .thenReturn(autor);

        when(autorMapper.toDto(autor))
                .thenReturn(autorResponseDTO);

        AutorResponseDTO salvarAutor = autorService.salvarAutor(autorRequestDTO);

        assertEquals(
                5L,
                salvarAutor.getId()
        );

        assertEquals(
                "João Pedro",
                salvarAutor.getNome()
        );

        assertEquals(
                "Brasileiro",
                salvarAutor.getNacionalidade()
        );
    }

    @Test
    public void listarAutores_quandoExistemAutores_retornaPaginaDeDtos(){
        Pageable pageable = PageRequest.of(0,10);

        Autor autor = new Autor();

        autor.setId(7L);
        autor.setNome("Pedro");
        autor.setNacionalidade("Ucraniano");
        autor.setLivros(List.of());

        AutorResponseDTO autorResponseDTO = new AutorResponseDTO();

        autorResponseDTO.setId(7L);
        autorResponseDTO.setNome("Pedro");
        autorResponseDTO.setNacionalidade("Ucraniano");
        autorResponseDTO.setLivros(List.of());

        Autor autor1 = new Autor();

        autor1.setId(8L);
        autor1.setNome("Henrique");
        autor1.setNacionalidade("Argentino");
        autor1.setLivros(List.of());

        AutorResponseDTO autorResponseDTO1 = new AutorResponseDTO();

        autorResponseDTO1.setId(8L);
        autorResponseDTO1.setNome("Henrique");
        autorResponseDTO1.setNacionalidade("Argentino");
        autorResponseDTO1.setLivros(List.of());


        Page<Autor> paginaAutores = new PageImpl<>(List.of(autor,autor1),pageable,2);

        when(autorRepository.findAll(pageable))
                .thenReturn(paginaAutores);

        when(autorMapper.toDto(autor))
                .thenReturn(autorResponseDTO);

        when(autorMapper.toDto(autor1))
                .thenReturn(autorResponseDTO1);

        autorService.listarAutor(pageable);

        assertEquals(
                1,
                paginaAutores.getTotalPages()
        );

        assertEquals(
                "Pedro",
                autor.getNome()
        );

        assertEquals(
                "Henrique",
                autor1.getNome()
        );
    }
}
