//CLASSE PARA A CRIAÇÃO DE NOVOS USUARIOS
package com.systech.springsecurity.controller;

import com.systech.springsecurity.controller.dto.CreateUSerDto;
import com.systech.springsecurity.entities.Role;
import com.systech.springsecurity.entities.Users;
import com.systech.springsecurity.repository.RoleRepository;
import com.systech.springsecurity.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
public class UserController {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PostMapping("/cadastro")
    public ResponseEntity<Void> newUser(@RequestBody CreateUSerDto dto){

        //VAI VINCULAR O USUARIO COMO USUARIO BASICO
        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        var userFromDb = userRepository.findByUsername(dto.username());

        //SE EXISTIR UM USUARIO COM O MESMO USERNAME, VAI DAR UM ERRO
        if (userFromDb.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new Users();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));

        userRepository.save(user);

        return  ResponseEntity.ok().build();
    }
}
