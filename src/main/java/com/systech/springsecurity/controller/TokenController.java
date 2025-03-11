package com.systech.springsecurity.controller;

import com.nimbusds.jwt.JWTClaimsSet;
import com.systech.springsecurity.controller.dto.LoginRequest;
import com.systech.springsecurity.controller.dto.LoginResponse;
import com.systech.springsecurity.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class TokenController {
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    //CRIANDO OS TOKENS PARA A SEGURANÇA DOS DADOS
    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.jwtEncoder=jwtEncoder;

        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
    }

    //RECEBER DADOS DO LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        var user = userRepository.findByUsername(loginRequest.username());

        //vai ir lá na classe Users e fazer a verificação de senha para ver se a descriptografada e a criptografada são iguais
        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)){
            throw new BadCredentialsException("User or Password is invalid");
        }

        var now = Instant.now();
        var expiresIn = 300L; //o tempo de resposta tem um limite de 5 minutos

        var clains = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(clains)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
