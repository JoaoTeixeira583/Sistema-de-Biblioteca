package com.example.SistemaBiblioteca_api.dto;


import com.example.SistemaBiblioteca_api.entity.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroSalvarDto {
    @NotBlank
    private String nome;

    @NotBlank
    private String isbn;

    @NotNull
    private LocalDate dataPublicacao;

    @NotNull
    private Long autorId;
}
