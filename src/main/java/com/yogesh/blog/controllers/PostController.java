package com.yogesh.blog.controllers;


import com.yogesh.blog.entities.Comment;
import com.yogesh.blog.entities.Post;
import com.yogesh.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {
    private final PostService blogPostService;

    @Autowired
    public PostController(PostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping("feed")
    String getPosts(Model model) {
        List<Post> posts = blogPostService.getBlogPostList();
        model.addAttribute("posts", posts);
        return "feed";
    }

    @GetMapping("post")
    String fetchPost(@RequestParam("id") String id, Model model) {
        Comment comment = new Comment();
        Optional<Post> blogPost = blogPostService.getPost(Integer.parseInt(id));
        Post post = blogPost.orElseGet(Post::new);
        comment.setPost(post);
        model.addAttribute("post", post);
        model.addAttribute("comment",comment);
        return "post";
    }

    @PostMapping("post")
    String savePost(@ModelAttribute("post") Post blogPost) {
        blogPostService.addPost(blogPost);
        return "redirect:/feed";
    }

    @PatchMapping("post")
    String updatePost(@ModelAttribute("post") Post blogPost) {
        blogPost.setUpdatedAt(LocalDateTime.now());
        blogPostService.addPost(blogPost);
        return "redirect:/feed";
    }

    @DeleteMapping("post")
    String deletePost() {
        return "redirect:/feed";
    }

    @GetMapping("edit-post")
    String fetchPostForUpdate(@RequestParam("id") String id, Model model) {
        Optional<Post> post = blogPostService.getPost(Integer.parseInt(id));
        model.addAttribute("post", post.orElseGet(Post::new));
        return "post-form";
    }

    @GetMapping("show-post-form")
    String showPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "post-form";
    }
}
