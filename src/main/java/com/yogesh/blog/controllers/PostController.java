package com.yogesh.blog.controllers;


import com.yogesh.blog.entities.Comment;
import com.yogesh.blog.entities.Post;
import com.yogesh.blog.entities.PostTag;
import com.yogesh.blog.entities.Tag;
import com.yogesh.blog.services.PostService;
import com.yogesh.blog.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {
    private final PostService postService;
    private final TagService tagService;

    @Autowired
    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("feed")
    String getPosts(@RequestParam(name = "start", defaultValue = "0", required = false) Integer start, @RequestParam(name = "limit", defaultValue = "4", required = false) Integer limit, @RequestParam(name = "sort-field", defaultValue = "publishedAt", required = false) String sortingField, @RequestParam(name = "order", defaultValue = "desc", required = false) String sortingOrder, @RequestParam(name = "search", required = false) String keyword, Model model) {
        List<Post> posts;

        if (start <= 0) {
            start = 0;
        }
        if (keyword == null) {
            posts = postService.getPostList(sortingField, sortingOrder, start, limit);
        } else {
            posts = postService.searchPost(keyword);
        }
        model.addAttribute("posts", posts);
        model.addAttribute("start", start);
        model.addAttribute("limit", limit);
        model.addAttribute("sortingField", sortingField);
        model.addAttribute("sortingOrder", sortingOrder);
        return "feed";
    }

    @GetMapping("show-post")
    String fetchPost(@RequestParam("id") Integer id, Model model) {
        Comment comment = new Comment();
        Optional<Post> blogPost = postService.getPost(id);

        Post post = blogPost.orElseGet(Post::new);
        comment.setPost(post);

        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        return "post";
    }

    @PostMapping("save-post")
    String savePost(@ModelAttribute("post") Post post, @RequestParam("tags") String tags) {
        if (post.getCreatedAt() == null) {
            post.setCreatedAt(LocalDateTime.now());
            post.setPublishedAt(LocalDateTime.now());
        } else {
            post.setUpdatedAt(LocalDateTime.now());
        }

        List<String> tagList = List.of(tags.split(" "));
        final List<PostTag> postTagList = new ArrayList<>();

        tagList.forEach(tag -> {
            Tag tagObj = new Tag();
            tagObj.setName(tag);
            tagObj.setCreatedAt(LocalDateTime.now());
            tagService.saveTag(tagObj);

            PostTag postTag = new PostTag();
            postTag.setTag(tagObj);
            postTag.setPost(post);
            postTag.setCreatedAt(LocalDateTime.now());
            postTagList.add(postTag);
        });
        post.setPostTags(postTagList);
        postService.addPost(post);
        return "redirect:/feed";
    }

    @GetMapping("delete-post")
    String deletePost(@RequestParam("id") Integer id) {
        postService.deletePost(id);
        return "redirect:/feed";
    }

    @GetMapping("edit-post")
    String fetchPostForUpdate(@RequestParam("id") Integer id, Model model) {
        Optional<Post> post = postService.getPost(id);
        model.addAttribute("post", post.orElseGet(Post::new));
        return "post-form";
    }

    @GetMapping("new-post")
    String showPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "post-form";
    }
}
