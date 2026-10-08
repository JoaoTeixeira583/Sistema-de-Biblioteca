package com.example.SistemaBiblioteca_api.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErroRespostaDto {

    private int status;
    private String mensagem;
    // uma lista contendo cada erro individuais de cada campo
    private List<ErroCampoDto> erros;

}
