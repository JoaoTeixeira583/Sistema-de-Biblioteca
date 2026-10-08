package com.example.SistemaBiblioteca_api.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErroCampoDto {

    private String campo;
    private String erro;
}
