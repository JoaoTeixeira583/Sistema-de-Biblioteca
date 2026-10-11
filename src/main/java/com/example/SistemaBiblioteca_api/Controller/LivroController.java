package com.example.SistemaBiblioteca_api.Controller;


import com.example.SistemaBiblioteca_api.dto.LivroResponseDto;
import com.example.SistemaBiblioteca_api.dto.LivroSalvarDto;
import com.example.SistemaBiblioteca_api.service.LivroService;
import jakarta.servlet.ServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    public ResponseEntity<LivroResponseDto> salvarLivro(@Valid @RequestBody LivroSalvarDto livroSalvarDto){
        LivroResponseDto livroResponseDto = livroService.salvarLivro(livroSalvarDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(livroResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDto> buscarLivroId(@PathVariable Long id){
        LivroResponseDto livroAchadoId = livroService.buscarLivroId(id);

        return ResponseEntity.ok(livroAchadoId);
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDto>> listarLivro(){
        List<LivroResponseDto> listaLivro = livroService.listarLivro();

        return ResponseEntity.ok(listaLivro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDto> atualizarLivro(@PathVariable Long id, @RequestBody LivroSalvarDto dto){
        LivroResponseDto livroEncontrado = livroService.atualizarLivro(id, dto);

       return ResponseEntity.ok(livroEncontrado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id){
        livroService.deletarLivro(id);

        return ResponseEntity.noContent().build();
    }




}
