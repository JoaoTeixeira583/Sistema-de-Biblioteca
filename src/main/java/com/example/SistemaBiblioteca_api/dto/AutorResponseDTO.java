package com.example.SistemaBiblioteca_api.dto;



import lombok.*;

import java.util.List;

// representa o que a api vai devolver para o cliente
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutorResponseDTO {

    private Long id;
    private String nome;
    private String nacionalidade;
    private List<LivroResumoDto> livros;

}
