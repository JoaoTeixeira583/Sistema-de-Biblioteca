package com.example.SistemaBiblioteca_api.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroResponseDto {


    private Long id;
    private String nome;
    private String isbn;
    private LocalDate dataPublicacao;
    private  AutorResponseDTO autor;


}
