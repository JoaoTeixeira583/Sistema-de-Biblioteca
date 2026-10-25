package com.example.SistemaBiblioteca_api.controller;


import com.example.SistemaBiblioteca_api.Controller.AutorController;
import com.example.SistemaBiblioteca_api.dto.AutorRequestDto;
import com.example.SistemaBiblioteca_api.dto.AutorResponseDto;
import com.example.SistemaBiblioteca_api.service.AutorService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutorController.class)
public class AutorControllerTest {

    @MockitoBean
    private AutorService autorService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void buscarAutorPorId_quandoAutorExiste_retornaStatus200ComDto() throws Exception{
        Long autorId = 1L;

        AutorResponseDto autorResponseDto = new AutorResponseDto();
        autorResponseDto.setId(1L);
        autorResponseDto.setNome("Pedro");
        autorResponseDto.setNacionalidade("Brasileiro");
        autorResponseDto.setLivros(List.of());

        when(autorService.buscarAutorId(autorId))
                .thenReturn(autorResponseDto);

        mockMvc.perform(get("/autores/{id}",autorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Pedro"));
    }

    @Test
    public void buscarAutorPorId_quandoAutorNaoExiste_retornaStatus404() throws Exception{
        Long autorId = 1L;

        when(autorService.buscarAutorId(autorId))
                .thenThrow(new EntityNotFoundException("Erro ao achar o autor"));

        mockMvc.perform(get("/autores/{id}",autorId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void salvarAutor_quandoDadosValidos_retornaStatus201ComDto() throws Exception{
        AutorRequestDto autorRequestDto = new AutorRequestDto();
        autorRequestDto.setNome("Pedro");
        autorRequestDto.setNacionalidade("Brasileiro");

        AutorResponseDto autorResponseDto = new AutorResponseDto();
        autorResponseDto.setId(1L);
        autorResponseDto.setNome("Pedro");
        autorResponseDto.setNacionalidade("Brasileiro");
        autorResponseDto.setLivros(List.of());

        when(autorService.salvarAutor(any(AutorRequestDto.class)))
                .thenReturn(autorResponseDto);

        mockMvc.perform(post("/autores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Pedro\",\"nacionalidade\":\"Brasileiro\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Pedro"))
                .andExpect(jsonPath("$.nacionalidade").value("Brasileiro"));
    }

    @Test
    public void salvarAutor_quandoDadosInvalidos_retornaStatus400() throws Exception{

        mockMvc.perform(post("/autores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"\",\"nacionalidade\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void atualizarAutor_quandoAutorExiste_retornaStatus200ComDto() throws Exception{
        Long autorId = 1L;

        AutorResponseDto autorResponseDto = new AutorResponseDto();
        autorResponseDto.setId(1L);
        autorResponseDto.setNome("Caio");
        autorResponseDto.setNacionalidade("Brasileiro");
        autorResponseDto.setLivros(List.of());

        when(autorService.atualizarAutor(eq(autorId),any(AutorRequestDto.class)))
                .thenReturn(autorResponseDto);

        mockMvc.perform(put("/autores/{id}",autorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Caio\",\"nacionalidade\":\"Brasileiro\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Caio"))
                .andExpect(jsonPath("$.nacionalidade").value("Brasileiro"));
    }

    @Test
    public void atualizarAutor_quandoAutorNaoExiste_retornaStatus404() throws Exception{
        Long aurtorId = 1L;

        when(autorService.atualizarAutor(eq(aurtorId),any(AutorRequestDto.class)))
                .thenThrow(new EntityNotFoundException("Erro ao encontrar autor"));

        mockMvc.perform(put("/autores/{id}",aurtorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Pedro\",\"nacionalidade\":\"Brasileiro\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletarAutor_quandoAutorExiste_retornaStatus204() throws Exception{
        Long autorId = 1L;

        mockMvc.perform(delete("/autores/{id}",autorId))
                .andExpect(status().isNoContent());

        verify(autorService).deletarAutor(autorId);
    }

    @Test
    public void deletarAutor_quandoAutorNaoExiste_retornaStatus404() throws Exception{
        Long autorId = 1L;

        doThrow(new EntityNotFoundException("Erro ao deletar autor"))
                .when(autorService)
                .deletarAutor(autorId);

        mockMvc.perform(delete("/autores/{id}",autorId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listarAutores_retornaStatus200ComPagina() throws Exception{
        AutorResponseDto autorResponseDto = new AutorResponseDto();
        autorResponseDto.setId(1L);
        autorResponseDto.setNome("Pedro");
        autorResponseDto.setNacionalidade("Brasileiro");
        autorResponseDto.setLivros(List.of());

        List<AutorResponseDto> listarAutores = List.of(autorResponseDto);

        Page<AutorResponseDto> paginasAutores = new PageImpl<>(listarAutores);

        when(autorService.listarAutor(any(Pageable.class)))
                .thenReturn(paginasAutores);

        mockMvc.perform(get("/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Pedro"));
    }

    @Test
    public void buscarAutoresPorNome_retornaStatus200ComPagina() throws Exception{
        String nome = "Pedro";
        Pageable pageable = PageRequest.of(0,10);

        AutorResponseDto autorResponseDto = new AutorResponseDto();
        autorResponseDto.setId(1L);
        autorResponseDto.setNome("Pedro");
        autorResponseDto.setNacionalidade("Brasileiro");
        autorResponseDto.setLivros(List.of());

        List<AutorResponseDto> lista = List.of(autorResponseDto);

        Page<AutorResponseDto> paginasAutores = new PageImpl<>(lista);

        when(autorService.buscarAutorNome(eq(nome),any(Pageable.class)))
                .thenReturn(paginasAutores);

        mockMvc.perform(get("/autores/busca")
                .param("nome",nome)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Pedro"));
    }

    @Test
    public void buscarAutoresPorNome_quandoNaoEncontraResultados_retornaPaginaVazia() throws Exception{
        String nome = "Pedro";
        Pageable pageable = PageRequest.of(0,10);

        AutorResponseDto autorResponseDto = new AutorResponseDto();
        autorResponseDto.setId(1L);
        autorResponseDto.setNome("Pedro");
        autorResponseDto.setNacionalidade("Brasileiro");
        autorResponseDto.setLivros(List.of());

        Page<AutorResponseDto> paginasAutores = new PageImpl<>(List.of(),pageable,0);

        when(autorService.buscarAutorNome(eq(nome),any(Pageable.class)))
                .thenReturn(paginasAutores);

        mockMvc.perform(get("/autores/busca")
                .param("nome",nome)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }
}
