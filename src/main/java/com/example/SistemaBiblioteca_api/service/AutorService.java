package com.example.SistemaBiblioteca_api.service;


import com.example.SistemaBiblioteca_api.dto.AutorRequestDTO;
import com.example.SistemaBiblioteca_api.dto.AutorResponseDTO;
import com.example.SistemaBiblioteca_api.entity.Autor;
import com.example.SistemaBiblioteca_api.mapper.AutorMapper;
import com.example.SistemaBiblioteca_api.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // para fazer o construtor na injeção
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;

    // metodo para salvar autor
    public AutorResponseDTO salvarAutor(AutorRequestDTO autorRequestDTO){
        Autor autor = autorMapper.toEntity(autorRequestDTO);
        Autor autorSalvo = autorRepository.save(autor);

        return autorMapper.toDto(autorSalvo);
    }

    public List<AutorResponseDTO> listarAutor(){
        List<Autor> listarAutor = autorRepository.findAll();

        List<AutorResponseDTO> autorResponseDTOList = listarAutor.stream()
                .map(autorMapper::toDto)
                .toList();

        return autorResponseDTOList;
    }

    // no Optional ja tem uma mensagem de erro
    public Optional<AutorResponseDTO> buscarAutorId(Long id){
        // o optional tem o metodo map para fazer a transfromação
        return autorRepository.findById(id)
                .map(autorMapper::toDto);
    }

    public void deletarAutor(Long id){
       if(autorRepository.existsById(id)){
           autorRepository.deleteById(id);
       } else {
           throw new RuntimeException("Informe um id de um autor que exista");
       }
    }

    public Optional<AutorResponseDTO> atualizarAutor(Long id,AutorRequestDTO autorRequestDTO) {
        return autorRepository.findById(id)
                .map(autor -> {
                      autor.setNome(autorRequestDTO.getNome());
                      autor.setNacionalidade(autorRequestDTO.getNacionalidade());

                      Autor autorAtualizado = autorRepository.save(autor);

                      return autorMapper.toDto(autorAtualizado);
                });
    }

}
