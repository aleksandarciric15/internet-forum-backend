package com.example.internetforum.controllers;

import com.example.internetforum.services.impl.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${internet.forum.api}/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok().body(this.permissionService.findAll());
    }

    @PostMapping("/{id}/find")
    public ResponseEntity<?> findById(@PathVariable Integer id){
        return ResponseEntity.ok().body(this.permissionService.findById(id));
    }

}
