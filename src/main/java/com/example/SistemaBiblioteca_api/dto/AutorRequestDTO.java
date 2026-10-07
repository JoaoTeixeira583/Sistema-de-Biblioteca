package com.example.SistemaBiblioteca_api.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.*;

// representa quais informações que vai entrar na api
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutorRequestDTO {
    @NotBlank
    private String nome;

    @NotBlank
    private String nacionalidade;

}
