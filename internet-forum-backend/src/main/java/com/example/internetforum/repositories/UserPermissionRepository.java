package com.example.internetforum.repositories;

import com.example.internetforum.models.entities.UserPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermissionEntity, Integer> {
}
