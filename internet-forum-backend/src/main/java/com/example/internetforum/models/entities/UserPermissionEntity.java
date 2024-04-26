package com.example.internetforum.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_permission")
@Data
public class UserPermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "permission_id", referencedColumnName = "id", nullable = false)
    private PermissionEntity permission;
}
