package com.example.SistemaBiblioteca_api.mapper;

import com.example.SistemaBiblioteca_api.dto.AutorRequestDto;
import com.example.SistemaBiblioteca_api.dto.AutorResponseDto;
import com.example.SistemaBiblioteca_api.dto.LivroResumoDto;
import com.example.SistemaBiblioteca_api.entity.Autor;
import org.springframework.stereotype.Component;

import java.util.List;

// para fazer transformação do dto para entity
@Component // diz ao spring que pode injetar
public class AutorMapper {

    // transformar dto em entity
    public Autor toEntity(AutorRequestDto autorRequestDTO) {

        Autor autor = new Autor();

        autor.setNome(autorRequestDTO.getNome());
        autor.setNacionalidade(autorRequestDTO.getNacionalidade());

        return autor;

    }

    // transformar enitty em dto
    public AutorResponseDto toDto(Autor autor){

        AutorResponseDto autorResponseDTO = new AutorResponseDto();

        autorResponseDTO.setId(autor.getId());
        autorResponseDTO.setNome(autor.getNome());
        autorResponseDTO.setNacionalidade(autor.getNacionalidade());

        List<LivroResumoDto> listaLivro = autor.getLivros() != null
                ? autor.getLivros().stream()
                .map(livro -> {
                    return LivroResumoDto.builder()
                            .id(livro.getId())
                            .nome(livro.getNome())
                            .isbn(livro.getIsbn())
                            .dataPublicacao(livro.getDataPublicacao())
                            .build();
                })
                .toList()
                :List.of();

        autorResponseDTO.setLivros(listaLivro);

        return autorResponseDTO;
    }

}
