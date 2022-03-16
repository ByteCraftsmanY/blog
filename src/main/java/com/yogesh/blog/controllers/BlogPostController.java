package com.yogesh.blog.controllers;


import com.yogesh.blog.entities.BlogPost;
import com.yogesh.blog.services.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class BlogPostController {
    private final BlogPostService blogPostService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping("feed")
    String getPosts(Model model) {
        List<BlogPost> posts = blogPostService.getBlogPostList();
        model.addAttribute("posts",posts);
        return "feed";
    }

    @GetMapping("post")
    String fetchPost(HttpServletRequest request, Model model){
        String id = request.getParameter("id");
            Optional<BlogPost> post = blogPostService.getPost(Integer.parseInt(id));
        model.addAttribute("post",post.get());
        return "blogpost";
    }

    @PostMapping("post")
    String savePost(@ModelAttribute("post") BlogPost blogPost) {
        blogPostService.addPost(blogPost);
        return "redirect:/feed";
    }

    @PatchMapping("post")
    String updatePost(@ModelAttribute("post") BlogPost blogPost) {
        blogPost.setUpdatedAt(LocalDateTime.now());
        blogPostService.addPost(blogPost);
        return "redirect:/feed";
    }

    @DeleteMapping("post")
    String deletePost(){
        return "redirect:/";
    }

    @GetMapping("editpost")
    String fetchPostForUpdate(HttpServletRequest request, Model model){
        Optional<BlogPost> post = blogPostService.getPost(Integer.parseInt(request.getParameter("id")));
        model.addAttribute("post",post.get());
        return "post-form";
    }

    @GetMapping("showpostform")
    String showPostForm(Model model){
        model.addAttribute("post",new BlogPost());
        return "post-form";
    }
}
