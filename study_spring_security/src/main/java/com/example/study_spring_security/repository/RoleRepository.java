package com.example.study_spring_security.repository;

import com.example.study_spring_security.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Object findByName(String name);
}
