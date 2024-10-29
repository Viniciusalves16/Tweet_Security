package com.example.study_spring_security.controller;

import com.example.study_spring_security.dto.LoginRequestDto;
import com.example.study_spring_security.dto.LoginResponsetDto;
import com.example.study_spring_security.entities.Role;
import com.example.study_spring_security.repository.UserRepository;
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
import java.util.stream.Collectors;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponsetDto> login(@RequestBody LoginRequestDto loginRequestDto) {

        var user = userRepository.findByUsername(loginRequestDto.username());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequestDto, passwordEncoder)
        ) {
            throw new BadCredentialsException("credentials invalid");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        // informações das roles do usuário para serem validadas na controller
        var scopes = user.get().getRole().stream().map(Role::getName)
                .collect(Collectors.joining(" "));

        //Gerar token KWT
        //Informações sobre a geração do conteudo do token
        var claims  = JwtClaimsSet.builder()
                .issuer("backend")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes).build();

        // Token utilizando a customização criada no claims
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return ResponseEntity.ok(new LoginResponsetDto(jwtValue,expiresIn));

    }


}
