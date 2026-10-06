package com.example.SistemaBiblioteca_api.service;


import com.example.SistemaBiblioteca_api.entity.Autor;
import com.example.SistemaBiblioteca_api.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // para fazer o construtor na injeção
public class AutorService {

    private final AutorRepository autorRepository;

    // metodo para salvar autor
    public Autor salvarAutor(Autor autor){
        return autorRepository.save(autor);
    }

    public List<Autor> listarAutor(){
        return autorRepository.findAll();
    }
    // no Optional ja tem uma mensagem de erro
    public Optional<Autor> buscarAutorId(Long id){
        return autorRepository.findById(id);
    }

    public void deletarAutor(Long id){
       autorRepository.deleteById(id);
    }

    public Autor atualizarAutor(Long id,Autor autor) {
        Optional<Autor> autorExistente = buscarAutorId(id);

        if (autorExistente.isPresent()) {
            // Você pega o Autor que estava dentro do Optional.
            Autor autorEncontrado = autorExistente.get();

            autorEncontrado.setNome(autor.getNome());
            autorEncontrado.setNacionalidade(autor.getNacionalidade());

            return autorRepository.save(autorEncontrado);
        } else {
            throw new RuntimeException("Informe um id de um autor que exista");
        }
    }

}
