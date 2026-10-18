package com.example.SistemaBiblioteca_api.Controller;


import com.example.SistemaBiblioteca_api.dto.LivroResponseDto;
import com.example.SistemaBiblioteca_api.dto.LivroRequestDto;
import com.example.SistemaBiblioteca_api.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    public ResponseEntity<LivroResponseDto> salvarLivro(@Valid @RequestBody LivroRequestDto livroRequestDto){
        LivroResponseDto livroResponseDto = livroService.salvarLivro(livroRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(livroResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDto> buscarLivroId(@PathVariable Long id){
        LivroResponseDto livroEncontrado = livroService.buscarLivroId(id);

        return ResponseEntity.ok(livroEncontrado);
    }

    @GetMapping()
    public ResponseEntity<Page<LivroResponseDto>> listarLivro(
            //  para deixar a paginação em default de um jeito casdo o cliente não escolha nada
            @PageableDefault(size = 10,sort = "nome",direction = Sort.Direction.ASC)
            Pageable pageable){

         Page<LivroResponseDto> paginaLivro = livroService.listarLivro(pageable);

         return ResponseEntity.ok(paginaLivro);
    }

    @GetMapping("/busca")
    public ResponseEntity<Page<LivroResponseDto>> buscarLivroNome(
            @RequestParam String nome,
            @PageableDefault(size = 10,sort = "nome",direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false)  Long autorId
    ){
        Page<LivroResponseDto> paginaLivroBusca = livroService.buscarLivroNome(nome,pageable,autorId);

        return ResponseEntity.ok(paginaLivroBusca);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDto> atualizarLivro(@PathVariable Long id, @RequestBody LivroRequestDto dto){
        LivroResponseDto livroEncontrado = livroService.atualizarLivro(id, dto);

         return ResponseEntity.ok(livroEncontrado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id){
        livroService.deletarLivro(id);

        return ResponseEntity.noContent().build();
    }
}
