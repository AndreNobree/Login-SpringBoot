package com.systech.springsecurity.entities;

import com.systech.springsecurity.controller.dto.LoginRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tb_users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)//gerar o id de forma automatica
    @Column(name = "user_id")
    private UUID userId;

    @Column(unique = true)
    private String username;

    private String password;

    //muitas para muitas
    //cascade vai replicar todas as modificações dessa na tabela user
    //fetch vai trazer sempre a role
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //criando uma tabela intermediaria para a relação das duas
    @JoinTable(
            name = "tb_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name  = "role_id")
    )
    private Set<Role> roles; //vai permitir que o usuario possua varias roles.


    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequest.password(), this.password); //compara a senha descriptografada(loginRequest.password()) com a criptografada
    }
}
