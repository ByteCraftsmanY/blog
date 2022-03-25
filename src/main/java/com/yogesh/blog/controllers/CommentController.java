package com.yogesh.blog.controllers;

import com.yogesh.blog.model.Comment;
import com.yogesh.blog.model.UserDetail;
import com.yogesh.blog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Controller
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("save-comment")
    public String saveComment(@ModelAttribute("comment") Comment comment) {
        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(LocalDateTime.now());
            if (!Objects.isNull(getLoggedInUser())) {
                comment.setUser(getLoggedInUser().getUser());
            }
        } else {
            comment.setUpdatedAt(LocalDateTime.now());
        }
        commentService.saveComment(comment);
        return "redirect:/show-post?id=" + comment.getPost().getId();
    }

    @GetMapping("delete-comment")
    public String deleteCommentById(@RequestParam("id") Integer id, @RequestParam("post-id") String postId) {
        Comment comment = commentService.findCommentById(id);
        if (!isAuthorizedUserForCommentOperation(comment)) {
            return "error";
        }
        commentService.deleteCommentById(id);
        return "redirect:/show-post?id=" + postId;
    }

    @GetMapping("update-comment")
    public String updateCommentById(@RequestParam("id") Integer id, Model model) {
        Comment comment = commentService.findCommentById(id);
        if (!isAuthorizedUserForCommentOperation(comment)) {
            return "error";
        }
        model.addAttribute("comment", comment);
        return "comment-form";
    }

    private UserDetail getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetail) {
            return (UserDetail) principal;
        }
        return null;
    }

    private Boolean isAuthorizedUserForCommentOperation(Comment comment) {
        UserDetail activeUserDetail = getLoggedInUser();
        return activeUserDetail == null || activeUserDetail.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || Objects.equals(activeUserDetail.getUser().getId(), comment.getUser().getId());
    }
}
