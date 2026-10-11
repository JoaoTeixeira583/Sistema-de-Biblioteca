package com.example.SistemaBiblioteca_api.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String nacionalidade;

    // para falar que um autor tem muitos livros
    @OneToMany(mappedBy = "autor")
    // e o mappedBy mostra que ja e mapeado pelo atributo autor na classe livro
    private List<Livro> livros;
}
