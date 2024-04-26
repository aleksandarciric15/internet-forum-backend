package com.example.internetforum.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "log")
@Data
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "message", nullable = false)
    private String message;

    @Basic
    @Column(name = "logger", nullable = false)
    private String logger;

    @Basic
    @Column(name = "level", nullable = false)
    private String level;

    @Basic
    @Column(name = "date", nullable = false)
    private Date date;
}
