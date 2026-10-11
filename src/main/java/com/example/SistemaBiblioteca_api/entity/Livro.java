package com.example.SistemaBiblioteca_api.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private LocalDate dataPublicacao;

    @ManyToOne
    @JoinColumn(name = "autor_id",nullable = false)
    private Autor autor;
}
