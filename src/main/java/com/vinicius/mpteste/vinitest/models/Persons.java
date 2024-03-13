package com.vinicius.mpteste.vinitest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = Persons.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Persons {

    public Persons(String nome, String ultimoNome, String email, String sexo, String ipAcesso, int idade, String nascimento) {
        this.nome = nome;
        this.ultimoNome = ultimoNome;
        this.email = email;
        this.sexo = sexo;
        this.ipAcesso = ipAcesso;
        this.idade = idade;
        this.nascimento = nascimento;
    }

    public static final String TABLE_NAME = "persons";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "nome", length = 100)
    @JsonProperty("Nome")
    private String nome;

    @Column(name = "ultimoNome", length = 100)
    @JsonProperty("UltimoNome")
    private String ultimoNome;

    @Column(name = "email", length = 100)
    @JsonProperty("Email")
    private String email;

    @Column(name = "sexo", length = 30)
    @JsonProperty("Sexo")
    private String sexo;

    @Column(name = "ipAcesso", length = 100)
    @JsonProperty("IpAcesso")
    private String ipAcesso;

    @Column(name = "idade")
    @JsonProperty("Idade")
    private int idade;

    @Column(name = "nascimento")
    @JsonProperty("Nascimento")
    private String nascimento;

}
