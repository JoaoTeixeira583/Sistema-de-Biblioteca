package com.example.SistemaBiblioteca_api.dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroResumoDto {

    private Long id;
    private String nome;
    private String isbn;
    private LocalDate dataPublicacao;
}
