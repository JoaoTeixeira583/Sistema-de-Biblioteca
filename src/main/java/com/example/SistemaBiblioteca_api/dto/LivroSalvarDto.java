package com.example.SistemaBiblioteca_api.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
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
    //Pattern para so aceitar exatamente 13 digitos
    //^ para não aceitar mais caracteres no começo
    // $ para não aceitar mais caracteres no final
    @Pattern(regexp = "^\\d{13}$",message = "ISBN deve conter exatamente 13 dígitos.")
    private String isbn;

    @NotNull
    //para não deixar colocar data futuras
    @PastOrPresent(message = "A data de publicação não pode ser futura")
    private LocalDate dataPublicacao;

    @NotNull
    private Long autorId;
}
