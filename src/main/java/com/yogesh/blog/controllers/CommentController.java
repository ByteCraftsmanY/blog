package com.yogesh.blog.controllers;

import com.yogesh.blog.exceptions.EntityNotFoundException;
import com.yogesh.blog.exceptions.UnauthorizedException;
import com.yogesh.blog.models.Comment;
import com.yogesh.blog.services.CommentService;
import com.yogesh.blog.services.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserPrincipalService userPrincipalService;

    @Autowired
    public CommentController(CommentService commentService, UserPrincipalService userPrincipalService) {
        this.commentService = commentService;
        this.userPrincipalService = userPrincipalService;
    }

    @GetMapping("{id}")
    Comment getCommentById(@PathVariable Integer id) {
        return commentService.findCommentById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    }

    @PostMapping
    public Comment saveComment(@RequestBody Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        if (!Objects.isNull(userPrincipalService.getUserPrincipal())) {
            comment.setUser(userPrincipalService.getUserPrincipal().getUser());
        }
        commentService.saveComment(comment);
        return comment;
    }

    @DeleteMapping("{id}")
    public void deleteCommentById(@PathVariable Integer id) {
        Comment comment = commentService.findCommentById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found."));
        if (!userPrincipalService.isUserAuthorizedForCommentOperation(comment)) {
            throw new UnauthorizedException("You are not authorized to to do this.");
        }
        commentService.deleteCommentById(id);
    }

    @PatchMapping("{id}")
    public Comment updateCommentById(@PathVariable Integer id, @RequestBody Map<Object, Object> commentFields) {
        Comment comment = commentService.findCommentById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        if (!userPrincipalService.isUserAuthorizedForCommentOperation(comment)) {
            throw new UnauthorizedException("You are not authorized to do this task");
        }
        commentFields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Comment.class, (String) key);
            Objects.requireNonNull(field).setAccessible(true);
            ReflectionUtils.setField(field, comment, value);
        });
        comment.setUpdatedAt(LocalDateTime.now());
        return commentService.saveComment(comment);
    }
}