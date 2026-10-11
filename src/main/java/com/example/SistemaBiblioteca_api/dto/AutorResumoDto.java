package com.example.SistemaBiblioteca_api.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutorResumoDto {

    private String nome;
    private String nacionalidade;
    private Long autorId;
}
