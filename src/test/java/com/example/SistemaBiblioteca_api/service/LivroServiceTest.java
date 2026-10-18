package com.example.SistemaBiblioteca_api.service;


import com.example.SistemaBiblioteca_api.dto.LivroRequestDto;
import com.example.SistemaBiblioteca_api.dto.LivroResponseDto;
import com.example.SistemaBiblioteca_api.entity.Autor;
import com.example.SistemaBiblioteca_api.entity.Livro;
import com.example.SistemaBiblioteca_api.mapper.LivroMapper;
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


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LivroServiceTest {

    @InjectMocks
    private LivroService livroService;

    @Mock
    private LivroRepository livroRepository;
    @Mock
    private AutorRepository autorRepository;
    @Mock
    private LivroMapper livroMapper;

    @Test
    public void buscarLivroNome_quandoEncontraLivro_retornaPaginaComLivro(){
        Long autorId = 4L;
        String nome = "O Senhor dos Anéis";
        Pageable pageable = PageRequest.of(0,10);

        Livro livro = new Livro();
        livro.setNome("O Senhor dos Anéis");

        LivroResponseDto livroResponseDto = new LivroResponseDto();
        livroResponseDto.setNome("O Senhor dos Anéis");

        Page<Livro> paginaLivro = new PageImpl<>(List.of(livro),pageable,1);

        when(livroMapper.toDto(livro))
                .thenReturn(livroResponseDto);

        when(autorRepository.existsById(autorId))
                .thenReturn(true);

        when(livroRepository.findByNomeContainingIgnoreCaseAndAutorId(nome, autorId, pageable))
                .thenReturn(paginaLivro);

        Page<LivroResponseDto> paginasLivros =  livroService.buscarLivroNome(nome,pageable,autorId);

        assertEquals(
                1,paginasLivros.getNumberOfElements()
        );

        assertEquals(
                "O Senhor dos Anéis",
                paginasLivros.getContent().get(0).getNome()
        );
    }

    @Test
    public void buscarLivroNome_quandoNaoEncontraLivro_retornaPaginaVazia(){
        Long autorId = 3L;
        String nome = "Quebra-Nozes";
        Pageable pageable = PageRequest.of(0,10);

        Page<Livro> paginaLivro = new PageImpl<>(List.of(),pageable,0);

        when(autorRepository.existsById(autorId))
                .thenReturn(true);

        when(livroRepository.findByNomeContainingIgnoreCaseAndAutorId(nome, autorId, pageable))
                .thenReturn(paginaLivro);

        Page<LivroResponseDto> paginasLivros = livroService.buscarLivroNome(nome,pageable,autorId);

        assertTrue(
                paginasLivros.isEmpty()
        );
    }

    @Test
    public void buscarLivroId_quandoLivroNaoExiste_lancaEntityNotFoundException(){
        Long idLivro = 99L;

        when(livroRepository.findById(idLivro))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> livroService.buscarLivroId(idLivro)
        );
    }

    @Test
    public void buscarLivroId_quandoLivroExiste_retornaDto(){
        Long id = 2L;

        Livro livro = new Livro();
        livro.setId(2L);
        livro.setNome("A Hora da Estrela");
        livro.setIsbn("978-85-250-5612-2");

        LivroResponseDto livroResponseDto = new LivroResponseDto();
        livroResponseDto.setId(2L);
        livroResponseDto.setNome("A Hora da Estrela");
        livroResponseDto.setIsbn("978-85-250-5612-2");

        when(livroRepository.findById(id))
                .thenReturn(Optional.of(livro));

        when(livroMapper.toDto(livro)).thenReturn(livroResponseDto);

        LivroResponseDto livroId = livroService.buscarLivroId(id);

        assertEquals(
                2L,livroId.getId()
        );

        assertEquals(
                "A Hora da Estrela",
                livroId.getNome()
        );
    }

    @Test
    public void atualizarLivro_quandoLivroNaoExiste_lancaEntityNotFoundException(){
        Long id = 99L;
        Long autorId = 99L;

        LivroRequestDto livroRequestDto = LivroRequestDto.builder()
                .nome("Vingadores")
                .isbn("34538457384")
                .dataPublicacao(LocalDate.parse("1954-07-29"))
                .autorId(99L)
                .build();

        Autor autor = new Autor();
        autor.setId(99L);
        autor.setNome("João");
        autor.setNacionalidade("Brasileiro");
        autor.setLivros(List.of());

        when(autorRepository.findById(autorId))
                .thenReturn(Optional.of(autor));

        when(livroRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> livroService.atualizarLivro(id,livroRequestDto)
        );
    }

    @Test
    public void atualizarLivro_quandoLivroExiste_retornaDtoAtualizado(){
        Long id = 2L;
        Long autorId = 2L;

        Livro livro = new Livro();
        livro.setId(2L);
        livro.setNome("O Senhor dos Anéis");
        livro.setIsbn("3653254623");
        livro.setDataPublicacao(LocalDate.parse("1954-07-29"));

        Autor autor = new Autor();
        autor.setId(2L);
        autor.setNome("Pedro");
        autor.setNacionalidade("Brasileiro");
        autor.setLivros(List.of());

        LivroRequestDto livroRequestDto = LivroRequestDto.builder()
                .nome("Gato de Botas")
                .isbn("789566965")
                .dataPublicacao(LocalDate.parse("1958-07-29"))
                .autorId(2L)
                .build();

        LivroResponseDto livroResponseDto = LivroResponseDto.builder()
                .id(2L)
                .nome("Gato de Botas")
                .isbn("789566965")
                .dataPublicacao(LocalDate.parse("1958-07-29"))
                .build();

        when(autorRepository.findById(autorId))
                .thenReturn(Optional.of(autor));
        when(livroRepository.findById(id))
                .thenReturn(Optional.of(livro));
        when(livroRepository.save(livro))
                .thenReturn(livro);
        when(livroMapper.toDto(livro)).thenReturn(livroResponseDto);

        LivroResponseDto livroId = livroService.atualizarLivro(id,livroRequestDto);

        assertEquals(
                2L,
                livroId.getId()
        );
        assertEquals(
                "Gato de Botas",
                livroId.getNome()
        );
        assertEquals(
                "789566965",
                livroId.getIsbn()
        );
        assertEquals(
                LocalDate.parse("1958-07-29"),
                livroId.getDataPublicacao()
        );
    }

    @Test
    public void listarLivros_quandoExistemLivros_retornaPaginaDeDtos(){
        Pageable pageable = PageRequest.of(0,10);

        Livro livro = new Livro();
        livro.setId(1L);
        livro.setNome("Quebra Nozes");
        livro.setIsbn("34576457654765");
        livro.setDataPublicacao(LocalDate.parse("1980-09-12"));

        LivroResponseDto livroResponseDto = new LivroResponseDto();
        livroResponseDto.setId(1L);
        livroResponseDto.setNome("Quebra Nozes");
        livroResponseDto.setIsbn("34576457654765");
        livroResponseDto.setDataPublicacao(LocalDate.parse("1980-09-12"));

        Livro livro1 = new Livro();
        livro1.setId(2L);
        livro1.setNome("Gatos");
        livro1.setIsbn("236546345");
        livro1.setDataPublicacao(LocalDate.parse("2000-03-21"));

        LivroResponseDto livroResponseDto1 = new LivroResponseDto();
        livroResponseDto1.setId(2L);
        livroResponseDto1.setNome("Gatos");
        livroResponseDto1.setIsbn("236546345");
        livroResponseDto1.setDataPublicacao(LocalDate.parse("2000-03-21"));

        Page<Livro> paginasLivros = new PageImpl<>(List.of(livro,livro1),pageable,2);

        when(livroRepository.findAll(pageable))
                .thenReturn(paginasLivros);
        when(livroMapper.toDto(livro)).thenReturn(livroResponseDto);

        when(livroMapper.toDto(livro1)).thenReturn(livroResponseDto1);

        Page<LivroResponseDto> paginasLivrosLista =  livroService.listarLivro(pageable);

        assertEquals(
                1,
                paginasLivrosLista.getTotalPages()
        );
        assertEquals(
                2,
                paginasLivrosLista.getNumberOfElements()
        );
        assertEquals(
                "Quebra Nozes",
                paginasLivrosLista.getContent().get(0).getNome()
        );
        assertEquals(
                "Gatos",
                paginasLivrosLista.getContent().get(1).getNome()
        );
    }

    @Test
    public void salvarLivro_quandoAutorExiste_salvaERetornaDto(){
        Long autorId = 2L;

        Autor autor = new Autor();
        autor.setId(2L);
        autor.setNome("Henrique");
        autor.setNacionalidade("Brasileiro");
        autor.setLivros(List.of());

        Livro livro = new Livro();
        livro.setId(1L);
        livro.setNome("Pedro Henrique");
        livro.setIsbn("4265234676523");
        livro.setDataPublicacao(LocalDate.parse("2008-08-24"));

        LivroResponseDto livroResponseDto = new LivroResponseDto();
        livroResponseDto.setId(1L);
        livroResponseDto.setNome("Pedro Henrique");
        livroResponseDto.setIsbn("4265234676523");
        livroResponseDto.setDataPublicacao(LocalDate.parse("2008-08-24"));

        LivroRequestDto livroRequestDto = new LivroRequestDto();
        livroRequestDto.setNome("Pedro Henrique");
        livroRequestDto.setIsbn("4265234676523");
        livroRequestDto.setDataPublicacao(LocalDate.parse("2008-08-24"));
        livroRequestDto.setAutorId(2L);

        when(livroRepository.save(livro))
                .thenReturn(livro);

        when(autorRepository.findById(autorId))
                .thenReturn(Optional.of(autor));

        when(livroMapper.toDto(livro)).thenReturn(livroResponseDto);

        when(livroMapper.toEntity(livroRequestDto)).thenReturn(livro);

        LivroResponseDto salvarLivro = livroService.salvarLivro(livroRequestDto);

        assertEquals(
                1L,
                salvarLivro.getId()
        );
        assertEquals(
                "Pedro Henrique",
                salvarLivro.getNome()
        );
        assertEquals(
                "4265234676523",
                salvarLivro.getIsbn()
        );
        assertEquals(
                LocalDate.parse("2008-08-24"),
                salvarLivro.getDataPublicacao()
        );
    }

    @Test
    public void salvarLivro_quandoAutorNaoExiste_lancaEntityNotFoundException(){
        Long autorId = 99L;

        Livro livro = new Livro();
        livro.setId(3L);
        livro.setNome("Gato de botas");
        livro.setIsbn("436754756");
        livro.setDataPublicacao(LocalDate.parse("2003-08-14"));

        LivroRequestDto livroRequestDto = new LivroRequestDto();
        livroRequestDto.setNome("Gato de botas");
        livroRequestDto.setIsbn("436754756");
        livroRequestDto.setDataPublicacao(LocalDate.parse("2003-08-14"));
        livroRequestDto.setAutorId(99L);

        when(autorRepository.findById(autorId))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () ->livroService.salvarLivro(livroRequestDto)
        );
    }

    @Test
    public void buscarLivroNome_quandoAutorIdNaoInformado_retornaResultados(){
        Long autorId = null;

        String nome = "O Senhor dos Anéis";
        Pageable pageable = PageRequest.of(0,10);

        Livro livro = new Livro();
        livro.setId(1L);
        livro.setNome("O Senhor dos Anéis");
        livro.setIsbn("346534765");
        livro.setDataPublicacao(LocalDate.parse("2008-09-13"));

        LivroResponseDto livroResponseDto = new LivroResponseDto();
        livroResponseDto.setId(1L);
        livroResponseDto.setNome("O Senhor dos Anéis");
        livroResponseDto.setIsbn("346534765");
        livroResponseDto.setDataPublicacao(LocalDate.parse("2008-09-13"));

        Page<Livro> paginaLivro = new PageImpl<>(List.of(livro),pageable,1);

        when(livroRepository.findByNomeContainingIgnoreCase(nome,pageable))
                .thenReturn(paginaLivro);

        when(livroMapper.toDto(livro))
                .thenReturn(livroResponseDto);

        Page<LivroResponseDto> paginasLivros = livroService.buscarLivroNome(nome,pageable,autorId);

        assertEquals(
                1,
                paginasLivros.getTotalElements()
        );
        assertEquals(
                "O Senhor dos Anéis",
                paginasLivros.getContent().get(0).getNome()
        );
    }

    @Test
    public void deletarLivro_quandoLivroExiste_removeLivro(){
        Long id = 1L;

        when(livroRepository.existsById(id))
                .thenReturn(true);

        livroService.deletarLivro(id);

        verify(livroRepository).deleteById(id);
    }

    @Test
    public void deletarLivro_quandoLivroNaoExiste_lancaEntityNotFoundException(){
        Long id = 33L;

        when(livroRepository.existsById(id))
                .thenReturn(false);

        assertThrows(
                EntityNotFoundException.class,
                () -> livroService.deletarLivro(id)
        );
    }

}
