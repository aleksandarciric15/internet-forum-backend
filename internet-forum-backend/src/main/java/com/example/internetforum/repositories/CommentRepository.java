package com.example.internetforum.repositories;

import com.example.internetforum.models.entities.CommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findAllByForumIdAndApprovedOrderByDateDesc(Integer forumId, boolean approved, Pageable pageable);
    List<CommentEntity> findAllByApprovedOrderByDateDesc(boolean approved);
}
