package com.example.internetforum.repositories;

import com.example.internetforum.models.entities.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Integer> {
    PermissionEntity findByName(String name);
}
