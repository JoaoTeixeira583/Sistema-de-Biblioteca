package com.example.SistemaBiblioteca_api.exception;


import com.example.SistemaBiblioteca_api.dto.ErroCampoDto;
import com.example.SistemaBiblioteca_api.dto.ErroRespostaDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

}
