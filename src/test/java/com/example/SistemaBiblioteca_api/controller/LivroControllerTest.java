package com.example.SistemaBiblioteca_api.controller;


import com.example.SistemaBiblioteca_api.Controller.LivroController;
import com.example.SistemaBiblioteca_api.dto.LivroRequestDto;
import com.example.SistemaBiblioteca_api.dto.LivroResponseDto;
import com.example.SistemaBiblioteca_api.service.LivroService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LivroController.class)
public class LivroControllerTest{
    @MockitoBean
    private LivroService livroService;

// fara as requisições de teste
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void buscarLivroPorId_quandoLivroExiste_retornaStatus200ComDto() throws Exception{
        Long id = 2L;

        LivroResponseDto livroResponseDto = new LivroResponseDto();
        livroResponseDto.setId(2L);
        livroResponseDto.setNome("Gato");
        livroResponseDto.setIsbn("47753443");
        livroResponseDto.setDataPublicacao(LocalDate.parse("2000-09-09"));

        when(livroService.buscarLivroId(id))
                .thenReturn(livroResponseDto);
        // simula uma requisição HTTP e confere a reposta do controller
        // simular o get que seria a url
        mockMvc.perform(get("/livros/{id}", id))
                // simula a reposta 200 ok
                .andExpect(status().isOk())
                // simula se retornando um id igual a 2
                .andExpect(jsonPath("$.id").value(2))
                // verifica se o nome igual ao Gato
                .andExpect(jsonPath("$.nome").value("Gato"));
    }

    @Test
    public void buscarLivroPorId_quandoLivroNaoExiste_retornaStatus404() throws Exception{
        Long id = 99L;

        // fazer um mock lançar um exceção
        when(livroService.buscarLivroId(id))
                .thenThrow(new EntityNotFoundException(("Livro não encontrado")));

        mockMvc.perform(get("/livros/{id}",id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void salvarLivro_quandoDadosValidos_retornaStatus201ComDto() throws Exception{
        LivroRequestDto livroRequestDto = new LivroRequestDto();
        livroRequestDto.setNome("Quebra Nozes");
        livroRequestDto.setIsbn("9781234567890");
        livroRequestDto.setDataPublicacao(LocalDate.parse("2009-09-17"));
        livroRequestDto.setAutorId(1L);

        LivroResponseDto livroResponseDto = new LivroResponseDto();
        livroResponseDto.setId(1L);
        livroResponseDto.setNome("Quebra Nozes");
        livroResponseDto.setIsbn("9781234567890");
        livroResponseDto.setDataPublicacao(LocalDate.parse("2009-09-17"));

        // any diz que o argumentop pode ser qualquer objeto desse tipo que vc colocar
        when(livroService.salvarLivro(any(LivroRequestDto.class)))
                .thenReturn(livroResponseDto);

        mockMvc.perform(post("/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Quebra Nozes\",\"isbn\":\"9781234567890\",\"dataPublicacao\":\"2009-09-17\",\"autorId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Quebra Nozes"));

    }

    @Test
    public void salvarLivro_quandoDadosInvalidos_retornaStatus400() throws Exception{

        mockMvc.perform(post("/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"\",\"isbn\":\"\",\"dataPublicacao\":null,\"autorId\":1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void atualizarLivro_quandoLivroExiste_retornaStatus200ComDto() throws Exception{
       Long id = 1L;

        LivroResponseDto livroResponseDto = new LivroResponseDto();
        livroResponseDto.setNome("Harry Potter");
        livroResponseDto.setIsbn("9356773465844");
        livroResponseDto.setDataPublicacao(LocalDate.parse("1989-07-17"));

        // eq serve para dizer o moCkito o primeiro que  precisa ser igual o id
        when(livroService.atualizarLivro(eq(id),any(LivroRequestDto.class)))
                .thenReturn(livroResponseDto);

        mockMvc.perform(put("/livros/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Harry Potter\",\"isbn\":\"9356773465844\",\"dataPublicacao\":\"1989-07-17\",\"autorId\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Harry Potter"))
                .andExpect(jsonPath("$.isbn").value("9356773465844"));
    }

    @Test
    public void atualizarLivro_quandoLivroNaoExiste_retornaStatus404() throws Exception{
        Long id = 99L;

        when(livroService.atualizarLivro(eq(id),any(LivroRequestDto.class)))
                .thenThrow(new EntityNotFoundException("Livro não existe"));

        mockMvc.perform(put("/livros/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Pequeno Pricncipe\",\"isbn\":\"1234567891012\",\"dataPublicacao\":\"2000-05-15\",\"autorId\":2}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletarLivro_quandoLivroExiste_retornaStatus204() throws Exception{
        Long id = 2L;

        mockMvc.perform(delete("/livros/{id}",id))
                        .andExpect(status().isNoContent());

        verify(livroService).deletarLivro(id);
    }

    @Test
    public void deletarLivro_quandoLivroNaoExiste_retornaStatus404() throws Exception{
        Long id = 33L;
        // para usar o doTrow com when
        // isso e para usar metodo que retornar um void
        doThrow(new EntityNotFoundException("Livro não encontrado"))
                .when(livroService)
                .deletarLivro(eq(id));

        mockMvc.perform(delete("/livros/{id}",id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listarLivros_retornaStatus200ComPagina() throws Exception{

        LivroResponseDto livroResponseDto = new LivroResponseDto();
        livroResponseDto.setNome("Machado de assis");
        livroResponseDto.setIsbn("1234567891011");
        livroResponseDto.setDataPublicacao(LocalDate.parse("2003-03-12"));

        List<LivroResponseDto> lista = List.of(livroResponseDto);

        Page<LivroResponseDto> paginaLivro = new PageImpl<>(lista);

        when(livroService.listarLivro(any(Pageable.class)))
                .thenReturn(paginaLivro);

        mockMvc.perform(get("/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Machado de assis"));
    }

    @Test
    public void buscarLivrosPorNome_retornaStatus200ComPagina() throws Exception{
        String nome = "Quebra Nozes";
        Pageable pageable = PageRequest.of(0,10);
        Long autorId = 1L;

        LivroResponseDto livroResponseDto = new LivroResponseDto();
        livroResponseDto.setNome("Quebra Nozes");
        livroResponseDto.setIsbn("4567834758734");
        livroResponseDto.setDataPublicacao(LocalDate.parse("2000-09-12"));

        List<LivroResponseDto> listaLivros = List.of(livroResponseDto);

        Page<LivroResponseDto> paginasLivros = new PageImpl<>(listaLivros,pageable,1);

        when(livroService.buscarLivroNome(eq(nome),any(Pageable.class),eq(autorId)))
                .thenReturn(paginasLivros);

        mockMvc.perform(get("/livros/busca")
                // funciona qunado precisa escrever algo na na url
                        .param("nome",nome)
                        .param("autorId",autorId.toString())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Quebra Nozes"));
    }

    @Test
    public void buscarLivrosPorNome_quandoAutorIdNaoInformado_retornaStatus200ComPagina() throws Exception {
        String nome = "Quebra Nozes";
        Pageable pageable = PageRequest.of(0, 10);
        Long autorId = null;

        LivroResponseDto livroResponseDto = new LivroResponseDto();
        livroResponseDto.setNome("Quebra Nozes");
        livroResponseDto.setIsbn("3425723723532");
        livroResponseDto.setDataPublicacao(LocalDate.parse("2005-05-13"));

        List<LivroResponseDto> lista = List.of(livroResponseDto);

        Page<LivroResponseDto> paginasLivros = new PageImpl<>(lista, pageable, 1);

        when(livroService.buscarLivroNome(eq(nome), any(Pageable.class), eq(autorId)))
                .thenReturn(paginasLivros);

        mockMvc.perform(get("/livros/busca")
                        .param("nome", nome)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Quebra Nozes"));
    }

    @Test
    public void buscarLivrosPorNome_quandoAutorIdNaoExiste_retornaStatus404() throws Exception {
        String nome = "Quebra Nozes";
        Pageable pageable = PageRequest.of(0, 10);
        Long autorId = 99L;

        LivroResponseDto livroResponseDto = new LivroResponseDto();
        livroResponseDto.setNome("Quebra Nozes");
        livroResponseDto.setIsbn("3474755743543");
        livroResponseDto.setDataPublicacao(LocalDate.parse("2002-02-19"));

        List<LivroResponseDto> lista = List.of(livroResponseDto);

        Page<LivroResponseDto> paginasLivros = new PageImpl<>(lista, pageable, 1);

        when(livroService.buscarLivroNome(eq(nome), any(Pageable.class), eq(autorId)))
                .thenThrow(new EntityNotFoundException("Autor não exise"));

        mockMvc.perform(get("/livros/busca")
                        .param("nome", nome)
                        .param("autorId",autorId.toString())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void buscarLivrosPorNome_quandoNaoEncontraResultados_retornaPaginaVazia() throws Exception{
        String nome = "Quebra Nozes";
        Pageable pageable = PageRequest.of(0,10);
        Long autorId = 1L;

        Page<LivroResponseDto> paginasLivros = new PageImpl<>(List.of(),pageable,0);

        when(livroService.buscarLivroNome(eq(nome),any(Pageable.class),isNull()))
                .thenReturn(paginasLivros);

        mockMvc.perform(get("/livros/busca")
                .param("nome",nome)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }
}

