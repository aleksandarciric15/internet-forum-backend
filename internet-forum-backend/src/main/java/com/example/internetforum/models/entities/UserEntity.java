package com.example.internetforum.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "user")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Basic
    @Column(name = "password", nullable = false)
    private String password;

    @Basic
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Basic
    @Column(name = "email", nullable = false)
    private String email;

    @Basic
    @Column(name = "blocked", nullable = false)
    private boolean blocked=false;

    @Basic
    @Column(name = "approved", nullable = false)
    private boolean approved=false; // admin approves that user can be registered/logged

    @Basic
    @Column(name = "verification_code", nullable = true)
    private String verificationCode; // user enters this code in second part of login process

    @Basic
    @Column(name = "role", nullable = false)
    private String role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserPermissionEntity> permissions;
}
