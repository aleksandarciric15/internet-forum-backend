package com.example.internetforum.controllers;

import com.example.internetforum.models.dto.AddCommentRequest;
import com.example.internetforum.models.dto.Comment;
import com.example.internetforum.models.dto.EditCommentRequest;
import com.example.internetforum.models.dto.Forum;
import com.example.internetforum.services.impl.CommentService;
import com.example.internetforum.services.impl.ForumService;
import com.example.internetforum.services.impl.WafService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${internet.forum.api}/forums")
public class ForumController {

    private final ForumService forumService;
    private final CommentService commentService;
    private final WafService wafService;

    public ForumController(ForumService forumService, CommentService commentService, WafService wafService) {
        this.forumService = forumService;
        this.commentService = commentService;
        this.wafService = wafService;
    }

    @GetMapping("/find-all-forums")
    public ResponseEntity<?> findAllForums() {
        List<Forum> forums = this.forumService.findAllForums();
        return ResponseEntity.ok().body(forums);
    }

    @GetMapping("/{id}/find-forum")
    public ResponseEntity<?> findForumById(@PathVariable Integer id) {
        Forum forum = this.forumService.findById(id);
        return ResponseEntity.ok().body(forum);
    }

    @GetMapping("/{id}/find-all-comments")
    public ResponseEntity<?> findAllByForumId(@PathVariable Integer id) {
        List<Comment> comments = this.commentService.findAllCommentsByForumId(id);
        return ResponseEntity.ok().body(comments);
    }

    @PostMapping("/add-comment")
    public ResponseEntity<?> addComment(@Valid @RequestBody AddCommentRequest request, BindingResult result) {
        this.wafService.checkRequest(result);
        this.commentService.addComment(request);
        return ResponseEntity.ok().body("");
    }

    @PutMapping("/edit-comment")
    public ResponseEntity<?> editComment(@Valid @RequestBody EditCommentRequest request, BindingResult result) {
        this.wafService.checkRequest(result);
        this.commentService.editComment(request);
        return ResponseEntity.ok().body("");
    }

    @DeleteMapping("/{id}/delete-comment")
    public ResponseEntity<?> deleteComment(@PathVariable Integer id) {
        this.commentService.deleteComment(id);
        return ResponseEntity.ok().body("");
    }

    @GetMapping("/comments/not-approved-comments")
    public ResponseEntity<?> findAllNotApproved(){
        List<Comment> comments = this.commentService.findAllNotApprovedComments();
        return ResponseEntity.ok().body(comments);
    }

    @PutMapping("/{id}/accept-comment")
    public ResponseEntity<?> acceptComment(@PathVariable Integer id) {
        this.commentService.acceptComment(id);
        return ResponseEntity.ok().body("");
    }

    @PutMapping("/comments/edi-comment")
    public ResponseEntity<?> editNotApprovedComment(@Valid @RequestBody EditCommentRequest request, BindingResult result){
        this.wafService.checkRequest(result);
        this.commentService.editComment(request);
        return ResponseEntity.ok().body("");
    }

    @DeleteMapping("/{id}/decline-comment")
    public ResponseEntity<?> declineComment(@PathVariable Integer id) {
        this.commentService.declineComment(id);
        return ResponseEntity.ok().body("");
    }
}
