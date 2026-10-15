package com.example.SistemaBiblioteca_api.service;


import com.example.SistemaBiblioteca_api.dto.AutorRequestDTO;
import com.example.SistemaBiblioteca_api.dto.AutorResponseDTO;
import com.example.SistemaBiblioteca_api.entity.Autor;
import com.example.SistemaBiblioteca_api.mapper.AutorMapper;
import com.example.SistemaBiblioteca_api.repository.AutorRepository;
import com.example.SistemaBiblioteca_api.repository.LivroRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor // para fazer o construtor na injeção
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;
    private final LivroRepository livroRepository;

    @Transactional
    public AutorResponseDTO salvarAutor(AutorRequestDTO autorRequestDTO){
        Autor autor = autorMapper.toEntity(autorRequestDTO);
        Autor autorSalvo = autorRepository.save(autor);

        return autorMapper.toDto(autorSalvo);
    }

    @Transactional(readOnly = true)
    public Page<AutorResponseDTO> listarAutor(Pageable pageable){
        Page<Autor> listarAutor = autorRepository.findAll(pageable);

        Page<AutorResponseDTO> paginaAutores = listarAutor
                .map(autorMapper::toDto);

        return paginaAutores;
    }

    public Page<AutorResponseDTO> buscarAutorNome(String nome,Pageable pageable){
        Page<Autor> paginaAutor = autorRepository.findByNomeContainingIgnoreCase(nome, pageable);

        Page<AutorResponseDTO> paginaAutorEncontrado = paginaAutor
                .map(autorMapper::toDto);

        return paginaAutorEncontrado;
    }

    // no Optional ja tem uma mensagem de erro
    @Transactional(readOnly = true)
    public AutorResponseDTO buscarAutorId(Long id){
        return autorRepository.findById(id)
                .map(autorMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Erro ao encontrar autor"));
    }

    @Transactional
    public AutorResponseDTO atualizarAutor(Long id,AutorRequestDTO autorRequestDTO) {
        return autorRepository.findById(id)
                .map(autor -> {
                    autor.setNome(autorRequestDTO.getNome());
                    autor.setNacionalidade(autorRequestDTO.getNacionalidade());

                    Autor autorAtualizado = autorRepository.save(autor);

                    return autorMapper.toDto(autorAtualizado);
                })
                .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado"));
    }

    @Transactional
    public void deletarAutor(Long id){
        if(!autorRepository.existsById(id)){
            throw new EntityNotFoundException("Erro ao encontrar o autor");
        } else if (livroRepository.existsByAutorId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Não é possível excluir um autor que possui livros");
        } else {
            autorRepository.deleteById(id);
        }
    }
}
