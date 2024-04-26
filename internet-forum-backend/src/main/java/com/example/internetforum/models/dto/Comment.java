package com.example.internetforum.models.dto;

import lombok.Data;

@Data
public class Comment {
    Integer id;
    String date;
    String comment;
    boolean approved;
    Integer forumId;
    CommentUser user;
}
