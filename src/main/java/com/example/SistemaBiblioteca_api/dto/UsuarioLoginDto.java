package com.example.SistemaBiblioteca_api.dto;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioLoginDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha;
}
