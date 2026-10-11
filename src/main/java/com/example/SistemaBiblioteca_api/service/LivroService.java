package com.example.SistemaBiblioteca_api.service;


import com.example.SistemaBiblioteca_api.dto.LivroResponseDto;
import com.example.SistemaBiblioteca_api.dto.LivroSalvarDto;
import com.example.SistemaBiblioteca_api.entity.Autor;
import com.example.SistemaBiblioteca_api.entity.Livro;
import com.example.SistemaBiblioteca_api.mapper.LivroMapper;
import com.example.SistemaBiblioteca_api.repository.AutorRepository;
import com.example.SistemaBiblioteca_api.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final LivroMapper livroMapper;

    public LivroResponseDto salvarLivro(LivroSalvarDto dto){
        Autor autorEncontrado = autorRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Erro ao encontrar autor"));

           Livro livro = livroMapper.toEntity(dto);
           livro.setAutor(autorEncontrado); // para ver o autor que tem esse livro

           Livro livroSalvo = livroRepository.save(livro);

           return livroMapper.toDto(livroSalvo);
    }

    public List<LivroResponseDto> listarLivro(){
        List<Livro> listarLivro = livroRepository.findAll();

        List<LivroResponseDto> listarLivroResponseDto = listarLivro.stream()
                .map(livroMapper::toDto)
                .toList();

        return listarLivroResponseDto;
    }

    public Optional<LivroResponseDto> buscarLivroId(Long id){
        return livroRepository.findById(id)
                .map(livroMapper::toDto);
    }

    public Optional<LivroResponseDto> atualizarLivro(Long id, LivroSalvarDto dto){
        Autor autorEncontrado = autorRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Erro ao encontrar autor"));

        return livroRepository.findById(id)
                .map(livro->{
                    livro.setNome(dto.getNome());
                    livro.setIsbn(dto.getIsbn());
                    livro.setDataPublicacao(dto.getDataPublicacao());
                    livro.setAutor(autorEncontrado);

                    Livro livroAtualizado = livroRepository.save(livro);

                    return livroMapper.toDto(livroAtualizado);
                });
    }

    public void  deletarLivro(long id){
         if(livroRepository.existsById(id)){
             livroRepository.deleteById(id);
         } else {
             throw new RuntimeException("Informe um id de um livro que exista");
         }
    }



}
