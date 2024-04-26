package com.example.internetforum.controllers;

import com.example.internetforum.models.dto.UserLoginRequest;
import com.example.internetforum.models.dto.UserRegisterRequest;
import com.example.internetforum.models.dto.VerificationUserRequest;
import com.example.internetforum.models.dto.VerificationUserResponse;
import com.example.internetforum.services.impl.AuthService;
import com.example.internetforum.services.impl.WafService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "${internet.forum.api}/auth")
public class AuthController {

    private final AuthService authService;
    private final WafService wafService;

    public AuthController(AuthService authService, WafService wafService) {
        this.authService = authService;
        this.wafService = wafService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest request, BindingResult result) {
        this.wafService.checkRequest(result);
        this.authService.registerUser(request);
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequest request, BindingResult result) {
        this.wafService.checkRequest(result);
        return ResponseEntity.ok().body(this.authService.loginUser(request));
    }

    @PostMapping("/confirm-login")
    public ResponseEntity<?> confirmLogin(@Valid @RequestBody VerificationUserRequest request, BindingResult result) {
        this.wafService.checkRequest(result);
        VerificationUserResponse response = this.authService.confirmLoginUser(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/github/login")
    public ResponseEntity<?> loginWithGithub(@RequestParam String code) throws IOException {
        VerificationUserResponse response = this.authService.loginUserWithGithub(code);
        return ResponseEntity.ok().body(response);
    }
}