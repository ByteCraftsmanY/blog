package com.yogesh.blog.controllers;

import com.yogesh.blog.model.Comment;
import com.yogesh.blog.model.User;
import com.yogesh.blog.model.UserDetail;
import com.yogesh.blog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
            comment.setUser(getActiveUser());
        } else {
            comment.setUpdatedAt(LocalDateTime.now());
        }
        commentService.saveComment(comment);
        return "redirect:/show-post?id=" + comment.getPost().getId();
    }

    @GetMapping("delete-comment")
    public String deleteCommentById(@RequestParam("id") Integer id, @RequestParam("post-id") String postId) {
        commentService.deleteCommentById(id);
        return "redirect:/show-post?id=" + postId;
    }

    @GetMapping("update-comment")
    public String updateCommentById(@RequestParam("id") Integer id, Model model) {
        Comment comment = commentService.findCommentById(id);
        model.addAttribute("comment", comment);
        return "comment-form";
    }

    private User getActiveUser() {
        User user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetail userDetail) {
            user = userDetail.getUser();
        }
        return user;
    }
}
