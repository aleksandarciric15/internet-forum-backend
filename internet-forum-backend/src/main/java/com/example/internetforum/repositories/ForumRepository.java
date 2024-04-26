package com.example.internetforum.repositories;

import com.example.internetforum.models.entities.ForumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumRepository extends JpaRepository<ForumEntity, Integer> {
}
