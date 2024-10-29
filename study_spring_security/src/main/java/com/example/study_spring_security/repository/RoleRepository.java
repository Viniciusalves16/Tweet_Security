package com.example.study_spring_security.repository;

import com.example.study_spring_security.entities.Role;
import com.example.study_spring_security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
