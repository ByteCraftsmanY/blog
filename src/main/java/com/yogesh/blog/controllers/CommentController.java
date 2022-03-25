package com.yogesh.blog.controllers;

import com.yogesh.blog.model.Comment;
import com.yogesh.blog.model.Post;
import com.yogesh.blog.model.User;
import com.yogesh.blog.model.UserPrincipal;
import com.yogesh.blog.services.CommentService;
import com.yogesh.blog.services.PostService;
import com.yogesh.blog.services.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final UserPrincipalService userPrincipalService;

    @Autowired
    public CommentController(CommentService commentService, UserPrincipalService userPrincipalService, PostService postService) {
        this.commentService = commentService;
        this.userPrincipalService = userPrincipalService;
        this.postService = postService;
    }

    @PostMapping("save-comment")
    public String saveComment(@ModelAttribute("comment") Comment comment) {
        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(LocalDateTime.now());
            if (!Objects.isNull(userPrincipalService.getLoggedInUser())) {
                comment.setUser(userPrincipalService.getLoggedInUser().getUser());
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
        if (!userPrincipalService.isUserAuthorizedForCommentOperation(comment)) {
            return "error";
        }
        commentService.deleteCommentById(id);
        return "redirect:/show-post?id=" + postId;
    }

    @GetMapping("update-comment")
    public String updateCommentById(@RequestParam("id") Integer id, Model model) {
        Comment comment = commentService.findCommentById(id);
        if (!userPrincipalService.isUserAuthorizedForCommentOperation(comment)) {
            return "error";
        }
        model.addAttribute("comment", comment);
        return "comment-form";
    }

    @GetMapping("new-comment")
    public String showFormForNewComment(@RequestParam("post-id") Integer postId, Model model) {
        User user = null;
        Post post = postService.findPostById(postId);
        UserPrincipal userPrincipal = userPrincipalService.getLoggedInUser();
        if (!Objects.isNull(userPrincipal)) {
            user = userPrincipal.getUser();
        }
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        model.addAttribute("comment", comment);
        return "comment-form";
    }
}
