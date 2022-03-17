package com.yogesh.blog.controllers;

import com.yogesh.blog.entities.Comment;
import com.yogesh.blog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("comment")
    public String getComment(@RequestParam("id") String id) {
        Optional<Comment> comment = commentService.fetchComment(Integer.parseInt(id));
        if(comment.isPresent())
            System.out.println(comment);
        return "feed";
    }

    @PostMapping("comment")
    public String saveComment(@ModelAttribute("comment") Comment comment){
        comment.setCreatedAt(LocalDateTime.now());
        commentService.saveComment(comment);
        return "redirect:/post?id="+comment.getPost().getId();
    }

}
