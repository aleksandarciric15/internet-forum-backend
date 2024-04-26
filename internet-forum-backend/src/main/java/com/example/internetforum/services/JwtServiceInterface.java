package com.example.internetforum.services;

import io.jsonwebtoken.Claims;

public interface JwtServiceInterface {
    String extractUsername(String jwt);
    Claims extractAllClaims(String token);
}
