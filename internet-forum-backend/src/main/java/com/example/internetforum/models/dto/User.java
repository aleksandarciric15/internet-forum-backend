package com.example.internetforum.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class User {
    Integer id;
    String username;
    String firstName;
    String lastName;
    String email;
    String role;
    boolean approved;
    boolean blocked;
    List<String> permissions;
}
