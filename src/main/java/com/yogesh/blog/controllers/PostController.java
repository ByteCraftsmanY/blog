package com.yogesh.blog.controllers;


import com.yogesh.blog.entities.Comment;
import com.yogesh.blog.entities.Post;
import com.yogesh.blog.entities.PostTag;
import com.yogesh.blog.entities.Tag;
import com.yogesh.blog.services.PostService;
import com.yogesh.blog.services.PostTagService;
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
    private final PostTagService postTagService;

    @Autowired
    public PostController(PostService postService, TagService tagService, PostTagService postTagService) {
        this.postService = postService;
        this.tagService = tagService;
        this.postTagService = postTagService;
    }

    @GetMapping("feed")
    String getPosts(@RequestParam(name = "page", defaultValue = "0", required = false) Integer page, @RequestParam(name = "limit", defaultValue = "2", required = false) Integer limit, @RequestParam(name = "order-by", defaultValue = "asc", required = false) String orderBy, Model model) {
        if(page <= 0){
            page = 0;
        }
        List<Post> posts = postService.getBlogPostList(page, limit, orderBy);
        model.addAttribute("posts", posts);
        model.addAttribute("page", page);
        model.addAttribute("limit", limit);
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

        tagList.forEach(tag->{
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
