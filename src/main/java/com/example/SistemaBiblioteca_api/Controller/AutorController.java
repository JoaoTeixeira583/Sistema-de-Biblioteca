package com.example.SistemaBiblioteca_api.Controller;


import com.example.SistemaBiblioteca_api.entity.Autor;
import com.example.SistemaBiblioteca_api.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    public ResponseEntity<Autor> salvarAutor(@Valid  @RequestBody Autor autor){
        Autor autorCriado = autorService.salvarAutor(autor);

        return ResponseEntity.status(HttpStatus.CREATED).body(autorCriado);
    }

    @GetMapping
    public ResponseEntity<List<Autor>> listarAutor(){
        List<Autor> listaAutores = autorService.listarAutor();

        return ResponseEntity.ok(listaAutores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> buscarAutorId(@PathVariable Long id){
        Optional<Autor> autorEncontrado = autorService.buscarAutorId(id);

        return autorEncontrado
                .map(autor -> ResponseEntity.ok(autor))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> atualizarAutor(@Valid @PathVariable Long id, @RequestBody Autor autor){
        Autor autorAtualizado = autorService.atualizarAutor(id,autor);

        return ResponseEntity.ok(autorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAutor(@PathVariable Long id){
        autorService.deletarAutor(id);

        return ResponseEntity.noContent().build();
    }

}
