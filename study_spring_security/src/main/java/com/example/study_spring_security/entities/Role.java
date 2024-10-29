package com.example.study_spring_security.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_roles")
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long RoleId;
    private String name;


    public enum Values {
        BASIC(2), ADMIN(1);
        long roleId;


        Values(long roleId) {
            this.roleId = roleId;
        }

    }
}
