package com.example.internetforum.controllers;

import com.example.internetforum.models.dto.ChangeUserPermissionsRequest;
import com.example.internetforum.models.dto.ChangeUserRoleRequest;
import com.example.internetforum.models.entities.LogEntity;
import com.example.internetforum.models.enums.LogLevel;
import com.example.internetforum.repositories.LogRepository;
import com.example.internetforum.services.impl.UserService;
import com.example.internetforum.services.impl.WafService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("${internet.forum.api}/users")
public class UserController {

    private final UserService userService;
    private final WafService wafService;
    private final LogRepository logRepository;

    public UserController(UserService userService, WafService wafService, LogRepository logRepository) {
        this.userService = userService;
        this.wafService = wafService;
        this.logRepository = logRepository;
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok().body(this.userService.findAll());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveUser(@PathVariable Integer id){
        this.userService.approveUser(id);
        return ResponseEntity.ok().body("");
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<?> blockUser(@PathVariable Integer id){
        this.userService.blockUser(id);
        return ResponseEntity.ok().body("");
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<?> unblockUser(@PathVariable Integer id){
        this.userService.unblockUser(id);
        return ResponseEntity.ok().body("");
    }

    @PutMapping("/change-role")
    public ResponseEntity<?> changeUserRole(@Valid @RequestBody ChangeUserRoleRequest request, BindingResult result){
        this.wafService.checkRequest(result);
        this.userService.changeUserRole(request);
        return ResponseEntity.ok().body("");
    }

    @PutMapping("/change-permissions")
    public ResponseEntity<?> changePermissions(@Valid @RequestBody ChangeUserPermissionsRequest request, BindingResult result){
        this.wafService.checkRequest(result);
        this.userService.changeUserPermissions(request);
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/logging")
    public void logMessage(){
        LogEntity logEntity = new LogEntity();
        logEntity.setId(null);
        logEntity.setMessage("asd");
        logEntity.setDate(new Date());
        logEntity.setLevel(LogLevel.WARNING.toString());
        logEntity.setLogger("logger1");
        this.logRepository.saveAndFlush(logEntity);
    }
}