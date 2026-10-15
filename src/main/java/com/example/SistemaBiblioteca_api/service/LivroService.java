package com.example.SistemaBiblioteca_api.service;


import com.example.SistemaBiblioteca_api.dto.LivroResponseDto;
import com.example.SistemaBiblioteca_api.dto.LivroSalvarDto;
import com.example.SistemaBiblioteca_api.entity.Autor;
import com.example.SistemaBiblioteca_api.entity.Livro;
import com.example.SistemaBiblioteca_api.mapper.LivroMapper;
import com.example.SistemaBiblioteca_api.repository.AutorRepository;
import com.example.SistemaBiblioteca_api.repository.LivroRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final LivroMapper livroMapper;

    public LivroResponseDto salvarLivro(LivroSalvarDto dto){
        Autor autorEncontrado = autorRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new EntityNotFoundException("Erro ao encontrar autor"));

           Livro livro = livroMapper.toEntity(dto);
           livro.setAutor(autorEncontrado); // para ver o autor que tem esse livro

           Livro livroSalvo = livroRepository.save(livro);

           return livroMapper.toDto(livroSalvo);
    }

    // page significa para lidar com grandes dados
    public Page<LivroResponseDto> listarLivro(Pageable pageable){
        Page<Livro> paginaLivro = livroRepository.findAll(pageable);

        Page<LivroResponseDto> paginasLivros = paginaLivro
                .map(livroMapper::toDto);

        return paginasLivros;
    }

    // metodo para buscar por nome do livro
    public Page<LivroResponseDto> buscarLivroNome(String nome,Pageable pageable,Long autorId){
        if(autorId==null){
            Page<Livro> buscarLivro = livroRepository.findByNomeContainingIgnoreCase(nome,pageable);

            Page<LivroResponseDto> paginaBuscaLivro = buscarLivro
                    .map(livroMapper::toDto);

            return paginaBuscaLivro;
        }else if(autorRepository.existsById(autorId)){
            Page<Livro> buscaLivroAutor = livroRepository.findByNomeContainingIgnoreCaseAndAutorId(nome, autorId, pageable);

            Page<LivroResponseDto> paginaBuscaLivroAutor = buscaLivroAutor
                    .map(livroMapper::toDto);

            return paginaBuscaLivroAutor;
        } else {
            throw new EntityNotFoundException("Erro ao achar o id do autor");
        }
    }

    public LivroResponseDto buscarLivroId(Long id){
        return livroRepository.findById(id)
                .map(livroMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Informe um id de um livro que exista"));
    }

    public LivroResponseDto atualizarLivro(Long id, LivroSalvarDto dto){
        Autor autorEncontrado = autorRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new EntityNotFoundException("Erro ao encontrar autor"));

        return livroRepository.findById(id)
                .map(livro->{
                    livro.setNome(dto.getNome());
                    livro.setIsbn(dto.getIsbn());
                    livro.setDataPublicacao(dto.getDataPublicacao());
                    livro.setAutor(autorEncontrado);

                    Livro livroAtualizado = livroRepository.save(livro);

                    return livroMapper.toDto(livroAtualizado);
                })
                .orElseThrow(()-> new EntityNotFoundException("Livro não encontrado"));
    }

    public void  deletarLivro(long id){
         if(livroRepository.existsById(id)){
             livroRepository.deleteById(id);
         } else {
             throw new EntityNotFoundException("Informe um id de um livro que exista");
         }
    }



}
