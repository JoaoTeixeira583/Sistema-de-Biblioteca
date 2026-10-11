package com.example.SistemaBiblioteca_api.service;


import com.example.SistemaBiblioteca_api.dto.AutorRequestDTO;
import com.example.SistemaBiblioteca_api.dto.AutorResponseDTO;
import com.example.SistemaBiblioteca_api.dto.LivroResponseDto;
import com.example.SistemaBiblioteca_api.dto.LivroSalvarDto;
import com.example.SistemaBiblioteca_api.entity.Autor;
import com.example.SistemaBiblioteca_api.entity.Livro;
import com.example.SistemaBiblioteca_api.repository.AutorRepository;
import com.example.SistemaBiblioteca_api.repository.LivroRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    public LivroResponseDto salvarLivro(LivroSalvarDto dto){
        Autor autorEncontrado = autorRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Erro ao encontrar autor"));

        Livro livro = new Livro();
       // para pegara as informações do banco de dados
        livro.setNome(dto.getNome());
        livro.setIsbn(dto.getIsbn());
        livro.setDataPublicacao(dto.getDataPublicacao());
        livro.setAutor(autorEncontrado);

        Livro livroSalvo = livroRepository.save(livro);

        // transformar o autor que foi encontrado com id numa reposta para o cliente
        AutorResponseDTO autorResponseDTO = AutorResponseDTO.builder()
                .id(autorEncontrado.getId())
                .nome(autorEncontrado.getNome())
                .nacionalidade(autorEncontrado.getNacionalidade())
                .build();

       // transformar o livro salvo numa reposta para o cliente
        LivroResponseDto livroResponseDto = LivroResponseDto.builder()
                .id(livroSalvo.getId())
                .nome(livroSalvo.getNome())
                .isbn(livroSalvo.getIsbn())
                .dataPublicacao(livroSalvo.getDataPublicacao())
                .autor(autorResponseDTO)
                .build();

        return livroResponseDto;
    }

    public LivroResponseDto buscarLivroId(Long id){
        return livroRepository.findById(id)
                .map(livro -> LivroResponseDto.builder()
                        .id(livro.getId())
                        .nome(livro.getNome())
                        .isbn(livro.getIsbn())
                        .dataPublicacao(livro.getDataPublicacao())
                        .autor(AutorResponseDTO.builder()
                                .id(livro.getAutor().getId())
                                .nome(livro.getAutor().getNome())
                                .nacionalidade(livro.getAutor().getNacionalidade())
                                .build())
                        .build()
                )
                .orElseThrow(() -> new EntityNotFoundException("Erro ao buscar o livro"));
    }

    public List<LivroResponseDto> listarLivro(){
        return livroRepository.findAll().stream()
                .map(listaLivro ->LivroResponseDto.builder()
                        .id(listaLivro.getId())
                        .nome(listaLivro.getNome())
                        .isbn(listaLivro.getIsbn())
                        .dataPublicacao(listaLivro.getDataPublicacao())
                        .autor(AutorResponseDTO.builder()
                                .id(listaLivro.getAutor().getId())
                                .nome(listaLivro.getAutor().getNome())
                                .nacionalidade(listaLivro.getAutor().getNacionalidade())
                                .build())
                        .build()
                )
                .toList();
    }

    public LivroResponseDto atualizarLivro(Long id, LivroSalvarDto dto){
        Livro livroExistente = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro ao encontrar livro"));

        Autor autorEncontrado = autorRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Erro ao encontrar autor"));

        livroExistente.setNome(dto.getNome());
        livroExistente.setIsbn(dto.getIsbn());
        livroExistente.setDataPublicacao(dto.getDataPublicacao());
        livroExistente.setAutor(autorEncontrado);

        Livro livroSalvo = livroRepository.save(livroExistente);

        AutorResponseDTO autorResponseDTO = AutorResponseDTO.builder()
                .id(autorEncontrado.getId())
                .nome(autorEncontrado.getNome())
                .nacionalidade(autorEncontrado.getNacionalidade())
                .build();

        LivroResponseDto livroResponseDto = LivroResponseDto.builder()
                .id(livroSalvo.getId())
                .nome(livroSalvo.getNome())
                .isbn(livroSalvo.getIsbn())
                .dataPublicacao(livroSalvo.getDataPublicacao())
                .autor(autorResponseDTO)
                .build();

        return livroResponseDto;
    }

    public void  deletarLivro(long id){
         if(livroRepository.existsById(id)){
             livroRepository.deleteById(id);
         } else {
             throw new RuntimeException("Informe um id de um livro que exista");
         }
    }



}
