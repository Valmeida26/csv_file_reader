package com.vinicius.mpteste.vinitest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vinicius.mpteste.vinitest.models.enuns.ProfileEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


//è tratato como uma tabela
@Entity
@Table(name = User.TABLE_NAME)
@AllArgsConstructor
//Construtor vazio
@NoArgsConstructor
//Ja tem nele o @Getter @Setter e @EqualsAndHashCode
@Data
//faz todos os getters
//@Getter
//faz todos os setters
//@Setter
//gera o equals and hashcodes
//@EqualsAndHashCode
public class User {

    public static final String TABLE_NAME = "user";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    @NotBlank()
    @Size(min = 2, max = 100)
    private String username;

    //Para a senha não ser retornada no front end
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", length = 60, nullable = false)
    @NotBlank
    @Size(min = 8, max = 60)
    private String password;

    //Garante que sempre que buscar o usuario traga os perfis do usuario junto
    @ElementCollection(fetch = FetchType.EAGER)
    //Garante que nao retorne ao usuario quais sao seus perfis
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @CollectionTable(name = "user_profile")
    @Column(name = "profile", nullable = false)
    //O set cria listas que não podem ter valores repetidos
    private Set<Integer> profiles = new HashSet<>();

    public Set<ProfileEnum> getProfiles(){
        return this.profiles.stream().map(x -> ProfileEnum.toEnum(x)).collect(Collectors.toSet());
    }

    public void addProfile(ProfileEnum profileEnum){
        this.profiles.add(profileEnum.getCode());
    }
}
