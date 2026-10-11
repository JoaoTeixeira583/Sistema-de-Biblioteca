package com.example.SistemaBiblioteca_api.exception;


import com.example.SistemaBiblioteca_api.dto.ErroCampoDto;
import com.example.SistemaBiblioteca_api.dto.ErroRespostaDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice // para saber todos os erros dos controllers
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDto> tratarErros(MethodArgumentNotValidException ex){

        List<ErroCampoDto> listaErroCampoDto = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> ErroCampoDto.builder()
                        .campo(fieldError.getField())
                        .erro(fieldError.getDefaultMessage())
                        .build()
                )
                .toList();

        ErroRespostaDto RepostaErroRespostaDto = ErroRespostaDto.builder()
                .status(400)
                .mensagem("Erro de validação nos campos")
                .erros(listaErroCampoDto)
                .build();

        return ResponseEntity.badRequest().body(RepostaErroRespostaDto);
    }

    // quando o spring lançar oq tem dentro dessa aspas ele lança esse metodo abaixo dele
    @ExceptionHandler({EntityNotFoundException.class, RuntimeException.class})
    public ResponseEntity<ErroRespostaDto> erroNaoEcontrado(RuntimeException ex){

        ErroRespostaDto erroRespostaDto = ErroRespostaDto.builder()
                .status(404)
                // pega a mensagem da validação do service e tranforma em json
                .mensagem(ex.getMessage())
                // para uma lista vazia
                .erros(List.of())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroRespostaDto);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroRespostaDto> erroConflict(ResponseStatusException ex){

        ErroRespostaDto erroRespostaDto = ErroRespostaDto.builder()
                // retorna o status HTTP associado à exceção, como 409 CONFLICT ou 404 NOT FOUND.
                .status(ex.getStatusCode().value())
                // retorna o motivo ou a mensagem que foi passada ao criar a ResponseStatusException. No seu caso, pode ser algo como “Não é possível excluir um autor que possui livros”.
                .mensagem(ex.getReason())
                .erros(List.of())
                .build();

        return ResponseEntity.status(ex.getStatusCode()).body(erroRespostaDto);
    }
}
