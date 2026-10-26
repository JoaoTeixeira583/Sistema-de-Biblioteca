package com.example.SistemaBiblioteca_api.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioCadastroDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8)
    private String senha;
}
