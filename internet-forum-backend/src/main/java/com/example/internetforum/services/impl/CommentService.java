package com.example.internetforum.services.impl;

import com.example.internetforum.exceptions.NotFoundException;
import com.example.internetforum.models.dto.AddCommentRequest;
import com.example.internetforum.models.dto.Comment;
import com.example.internetforum.models.dto.EditCommentRequest;
import com.example.internetforum.models.entities.CommentEntity;
import com.example.internetforum.models.entities.ForumEntity;
import com.example.internetforum.models.entities.UserEntity;
import com.example.internetforum.repositories.CommentRepository;
import com.example.internetforum.services.CommentServiceInterface;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService implements CommentServiceInterface {

    private static final int PAGE_SIZE = 20;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, SiemService siemService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addComment(AddCommentRequest request) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setComment(request.getComment());
        commentEntity.setDate(new Date());
        commentEntity.setApproved(false);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(request.getUserId());
        commentEntity.setUser(userEntity);
        ForumEntity forumEntity = new ForumEntity();
        forumEntity.setId(request.getForumId());
        commentEntity.setForum(forumEntity);

        this.commentRepository.saveAndFlush(commentEntity);
    }

    @Override
    public void acceptComment(Integer id) {
        CommentEntity commentEntity = this.commentRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        commentEntity.setApproved(true);
    }

    @Override
    public void deleteComment(Integer id) {
        if (!this.commentRepository.existsById(id)) {
            throw new NotFoundException();
        }

        this.commentRepository.deleteById(id);
    }

    @Override
    public void declineComment(Integer id) {
        if (this.commentRepository.findById(id).isPresent()) {
            this.commentRepository.deleteById(id);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public void editComment(EditCommentRequest request) {
        CommentEntity commentEntity = this.commentRepository.findById(request.getId())
                .orElseThrow(NotFoundException::new);
        commentEntity.setComment(request.getComment());
        commentEntity.setDate(new Date());
        commentEntity.setApproved(false);
    }

    @Override
    public List<Comment> findAllCommentsByForumId(Integer forumId) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        List<CommentEntity> commentEntities = this.commentRepository
                .findAllByForumIdAndApprovedOrderByDateDesc(forumId, true, pageable);
        return commentEntities.stream().map(elem -> this.modelMapper.map(elem, Comment.class))
                .collect(Collectors.toList());
    }

    public List<Comment> findAllNotApprovedComments() {
        List<CommentEntity> commentEntities = this.commentRepository
                .findAllByApprovedOrderByDateDesc(false);
        return commentEntities.stream().map(elem -> modelMapper.map(elem, Comment.class)).collect(Collectors.toList());
    }
}
