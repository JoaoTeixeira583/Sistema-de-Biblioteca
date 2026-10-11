package com.example.SistemaBiblioteca_api.mapper;

import com.example.SistemaBiblioteca_api.dto.AutorResumoDto;
import com.example.SistemaBiblioteca_api.dto.LivroResponseDto;
import com.example.SistemaBiblioteca_api.dto.LivroSalvarDto;
import com.example.SistemaBiblioteca_api.entity.Livro;
import org.springframework.stereotype.Component;



@Component
public class LivroMapper {

    public LivroResponseDto toDto(Livro livro){

        if(livro == null){
            return null;
        }

        LivroResponseDto livroResponseDto = new LivroResponseDto();

        livroResponseDto.setId(livro.getId());
        livroResponseDto.setNome(livro.getNome());
        livroResponseDto.setIsbn(livro.getIsbn());
        livroResponseDto.setDataPublicacao(livro.getDataPublicacao());

        livroResponseDto.setAutor(AutorResumoDto.builder()
                        .nome(livro.getAutor().getNome())
                        .nacionalidade(livro.getAutor().getNacionalidade())
                        .autorId(livro.getAutor().getId())
                        .build());

        return livroResponseDto;
    }

    public Livro toEntity(LivroSalvarDto livroSalvarDto){

        Livro livro = new Livro();


        livro.setNome(livroSalvarDto.getNome());
        livro.setIsbn(livroSalvarDto.getIsbn());
        livro.setDataPublicacao(livroSalvarDto.getDataPublicacao());


        return livro;
    }

}
