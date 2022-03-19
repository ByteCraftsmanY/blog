package com.yogesh.blog.controllers;


import com.yogesh.blog.entities.*;
import com.yogesh.blog.services.PostService;
import com.yogesh.blog.services.TagService;
import com.yogesh.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {
    private final PostService postService;
    private final TagService tagService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, TagService tagService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping("/")
    String getPostsWithCriteria(@RequestParam(name = "start", defaultValue = "0", required = false) Integer start, @RequestParam(name = "limit", defaultValue = "4", required = false) Integer limit, @RequestParam(name = "sort-field", defaultValue = "publishedAt", required = false) String sortingField, @RequestParam(name = "order", defaultValue = "desc", required = false) String sortingOrder, @RequestParam(name = "search", required = false) String keyword, @RequestParam(name = "filter", defaultValue = "false", required = false) Boolean filter, @RequestParam(name = "tags", required = false) String tags, @RequestParam(name = "start-date", required = false) String startDate, @RequestParam(name = "end-date", required = false) String endDate, @RequestParam(name = "author", defaultValue = "", required = false) String author, Model model) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        List<Post> posts;
        List<User> users = userService.findAllUsers();
        // paging
        if (start <= 0) {
            start = 0;
        }
        if (keyword != null) {
            posts = postService.findPostsHavingKeyword(keyword);
        } else if (filter) { // filter
            if (startDate == null || startDate.length() == 0) {
                startDate = "1970-01-01";
            }
            if (endDate == null || endDate.length() == 0) {
                endDate = LocalDate.now().plusDays(1).toString();
            }
            startDateTime = LocalDate.parse(startDate).atStartOfDay();
            endDateTime = LocalDate.parse(endDate).atStartOfDay();

            List<String> tagListForSearch;
            if (tags == null || tags.isEmpty()) {
                tagListForSearch = new ArrayList<>();
                for (Tag tag : tagService.findAllTags()) {
                    tagListForSearch.add(tag.getName());
                }
            } else {
                tagListForSearch = List.of(tags.split(" "));
            }

//            System.out.println("Filter for " + author + " -> " + startDate + " -> " + endDate + " >> " + tagListForSearch);

            posts = postService.findPostsWithCriteria(tagListForSearch, author, startDateTime, endDateTime);
        } else {
            posts = postService.findAllPost(sortingField, sortingOrder, start, limit);
        }
        model.addAttribute("posts", posts);
        model.addAttribute("start", start);
        model.addAttribute("limit", limit);
        model.addAttribute("sortingField", sortingField);
        model.addAttribute("sortingOrder", sortingOrder);
        model.addAttribute("users", users);
        return "home-page";
    }

    @GetMapping("show-post")
    String getPostById(@RequestParam("id") Integer id, Model model) {
        Post post = postService.findPostById(id);

        Comment comment = new Comment();
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
        List<PostTag> postTagList = new ArrayList<>();

        tagList.forEach(tagName -> {
            Tag tag = new Tag();
            tag.setName(tagName);
            tag.setCreatedAt(LocalDateTime.now());
            tagService.saveTag(tag);

            PostTag postTag = new PostTag();
            postTag.setTag(tag);
            postTag.setPost(post);
            postTag.setCreatedAt(LocalDateTime.now());
            postTagList.add(postTag);
        });
        post.setPostTags(postTagList);
        postService.savePost(post);
        return "redirect:/";
    }

    @GetMapping("delete-post")
    String deletePostById(@RequestParam("id") Integer id) {
        postService.deletePostById(id);
        return "redirect:/";
    }

    @GetMapping("edit-post")
    String fetchPostForUpdate(@RequestParam("id") Integer id, Model model) {
        Post post = postService.findPostById(id);
        model.addAttribute("post", post);
        return "post-form";
    }

    @GetMapping("new-post")
    String showFormForNewPost(Model model) {
        model.addAttribute("post", new Post());
        return "post-form";
    }
}
