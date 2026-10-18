package com.example.SistemaBiblioteca_api.Controller;


import com.example.SistemaBiblioteca_api.dto.AutorRequestDto;
import com.example.SistemaBiblioteca_api.dto.AutorResponseDto;
import com.example.SistemaBiblioteca_api.service.AutorService;
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
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    public ResponseEntity<AutorResponseDto> salvarAutor(@Valid  @RequestBody AutorRequestDto autorRequestDTO){
        AutorResponseDto autorCriado = autorService.salvarAutor(autorRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(autorCriado);
    }

    @GetMapping()
    public ResponseEntity<Page<AutorResponseDto>> listarAuto(
            @PageableDefault(size = 10,sort = "nome",direction = Sort.Direction.ASC)
            Pageable pageable){

        Page<AutorResponseDto> paginaAutor = autorService.listarAutor(pageable);

        return ResponseEntity.ok(paginaAutor);
    }

    @GetMapping("/busca")
    public ResponseEntity<Page<AutorResponseDto>> buscarAutorNome(
            @RequestParam String nome,
            @PageableDefault(size = 10,sort = "nome",direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        Page<AutorResponseDto> paginaAutorEncontrado = autorService.buscarAutorNome(nome, pageable);

        return ResponseEntity.ok(paginaAutorEncontrado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDto> buscarAutorId(@PathVariable Long id){
        AutorResponseDto autorEncontrado = autorService.buscarAutorId(id);

         return ResponseEntity.ok(autorEncontrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDto> atualizarAutor(@PathVariable Long id, @Valid @RequestBody AutorRequestDto autorRequestDTO){
        AutorResponseDto autorAtualizado = autorService.atualizarAutor(id,autorRequestDTO);

        return ResponseEntity.ok(autorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAutor(@PathVariable Long id){
        autorService.deletarAutor(id);

        return ResponseEntity.noContent().build();
    }

}
