package com.example.internetforum.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "forum")
@Data
public class ForumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;
}
