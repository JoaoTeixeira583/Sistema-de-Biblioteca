package com.example.SistemaBiblioteca_api.mapper;

import com.example.SistemaBiblioteca_api.dto.AutorRequestDTO;
import com.example.SistemaBiblioteca_api.dto.AutorResponseDTO;
import com.example.SistemaBiblioteca_api.entity.Autor;
import org.springframework.stereotype.Component;

// para fazer transformação do dto para entity
@Component // diz ao spring que pode injetar
public class AutorMapper {

    // transformar dto em entity
    public Autor toEntity(AutorRequestDTO autorRequestDTO) {

        Autor autor = new Autor();

        autor.setNome(autorRequestDTO.getNome());
        autor.setNacionalidade(autorRequestDTO.getNacionalidade());

        return autor;

    }

    // transformar enitty em dto
    public AutorResponseDTO toDto(Autor autor){

        AutorResponseDTO autorResponseDTO = new AutorResponseDTO();

        autorResponseDTO.setId(autor.getId());
        autorResponseDTO.setNome(autor.getNome());
        autorResponseDTO.setNacionalidade(autor.getNacionalidade());

        return autorResponseDTO;

    }

}
