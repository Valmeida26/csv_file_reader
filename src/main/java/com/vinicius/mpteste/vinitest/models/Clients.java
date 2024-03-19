package com.vinicius.mpteste.vinitest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = Clients.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Clients {

    public Clients(String name, String lastName, String email, String gender, String ipAccess, int age, String birthDate) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.ipAccess = ipAccess;
        this.age = age;
        this.birthDate = birthDate;
    }

    public static final String TABLE_NAME = "clients";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "name", length = 100)
    @JsonProperty("Nome")
    private String name;

    @Column(name = "lastName", length = 100)
    @JsonProperty("UltimoNome")
    private String lastName;

    @Column(name = "email", length = 100)
    @JsonProperty("Email")
    private String email;

    @Column(name = "gender", length = 30)
    @JsonProperty("Sexo")
    private String gender;

    @Column(name = "ipAccess", length = 100)
    @JsonProperty("IpAcesso")
    private String ipAccess;

    @Column(name = "age")
    @JsonProperty("Idade")
    private int age;

    @Column(name = "birthDate")
    @JsonProperty("Nascimento")
    private String birthDate;

}
