package com.example.SistemaBiblioteca_api.Controller;


import com.example.SistemaBiblioteca_api.dto.AutorRequestDTO;
import com.example.SistemaBiblioteca_api.dto.AutorResponseDTO;
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
    public ResponseEntity<AutorResponseDTO> salvarAutor(@Valid  @RequestBody AutorRequestDTO autorRequestDTO){
        AutorResponseDTO autorCriado = autorService.salvarAutor(autorRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(autorCriado);
    }

    @GetMapping()
    public ResponseEntity<Page<AutorResponseDTO>> listarAuto(
            @PageableDefault(size = 10,sort = "nome",direction = Sort.Direction.ASC)
            Pageable pageable){

        Page<AutorResponseDTO> paginaAutor = autorService.listarAutor(pageable);

        return ResponseEntity.ok(paginaAutor);
    }

    @GetMapping("/busca")
    public ResponseEntity<Page<AutorResponseDTO>> buscarAutorNome(
            @RequestParam String nome,
            @PageableDefault(size = 10,sort = "nome",direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        Page<AutorResponseDTO> paginaAutorEncontrado = autorService.buscarAutorNome(nome, pageable);

        return ResponseEntity.ok(paginaAutorEncontrado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscarAutorId(@PathVariable Long id){
        AutorResponseDTO autorEncontrado = autorService.buscarAutorId(id);

         return ResponseEntity.ok(autorEncontrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> atualizarAutor(@PathVariable Long id,@Valid @RequestBody AutorRequestDTO autorRequestDTO){
        AutorResponseDTO autorAtualizado = autorService.atualizarAutor(id,autorRequestDTO);

        return ResponseEntity.ok(autorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAutor(@PathVariable Long id){
        autorService.deletarAutor(id);

        return ResponseEntity.noContent().build();
    }

}
