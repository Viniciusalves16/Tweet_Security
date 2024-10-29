package com.example.study_spring_security.controller;

import com.example.study_spring_security.dto.CreateUserDto;
import com.example.study_spring_security.entities.Role;
import com.example.study_spring_security.entities.User;
import com.example.study_spring_security.repository.RoleRepository;
import com.example.study_spring_security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }


    @PostMapping("/users")
    @Transactional
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto createUserDto) {

        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
        var userFromDb = userRepository.findByUsername(createUserDto.userName());

        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setUsername(createUserDto.userName());
        user.setPassword(encoder.encode(createUserDto.password()));
        user.setRole(Set.of((Role) basicRole));
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") // Permite que você crie uma pré autorização baseado no scope do token JWT
    public ResponseEntity<List<User>> listUsers() {

        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
