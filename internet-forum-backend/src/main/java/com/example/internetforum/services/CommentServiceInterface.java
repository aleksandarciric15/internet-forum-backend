package com.example.internetforum.services;

import com.example.internetforum.models.dto.AddCommentRequest;
import com.example.internetforum.models.dto.Comment;
import com.example.internetforum.models.dto.EditCommentRequest;

import java.util.List;

public interface CommentServiceInterface {
    void addComment(AddCommentRequest request);
    void acceptComment(Integer id);
    void declineComment(Integer id);
    void deleteComment(Integer id);
    void editComment(EditCommentRequest request);
    List<Comment> findAllCommentsByForumId(Integer forumId);
}
