package com.example.SistemaBiblioteca_api.Controller;


import com.example.SistemaBiblioteca_api.dto.AutorRequestDTO;
import com.example.SistemaBiblioteca_api.dto.AutorResponseDTO;
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
    public ResponseEntity<AutorResponseDTO> salvarAutor(@Valid  @RequestBody AutorRequestDTO autorRequestDTO){
        AutorResponseDTO autorCriado = autorService.salvarAutor(autorRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(autorCriado);
    }

    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> listarAutor(){
        List<AutorResponseDTO> listaAutores = autorService.listarAutor();

        return ResponseEntity.ok(listaAutores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscarAutorId(@PathVariable Long id){
        Optional<AutorResponseDTO> autorEncontrado = autorService.buscarAutorId(id);

        return autorEncontrado
                .map(autor -> ResponseEntity.ok(autor))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> atualizarAutor(@PathVariable Long id,@Valid @RequestBody AutorRequestDTO autorRequestDTO){
        Optional<AutorResponseDTO> autorAtualizado = autorService.atualizarAutor(id,autorRequestDTO);

        return autorAtualizado
                .map(autorResponseDTO -> ResponseEntity.ok(autorResponseDTO))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAutor(@PathVariable Long id){
        autorService.deletarAutor(id);

        return ResponseEntity.noContent().build();
    }

}
