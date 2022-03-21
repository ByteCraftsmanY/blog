package com.yogesh.blog.controllers;

import com.yogesh.blog.entities.Comment;
import com.yogesh.blog.entities.Post;
import com.yogesh.blog.entities.Tag;
import com.yogesh.blog.services.PostService;
import com.yogesh.blog.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.Objects;

@Controller
public class PostController {
    private final PostService postService;
    private final TagService tagService;

    @Autowired
    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/")
    String getAllPosts(@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
                                @RequestParam(name = "size", defaultValue = "5", required = false) Integer size,
                                @RequestParam(name = "sort-field", defaultValue = "publishedAt", required = false) String sortingField,
                                @RequestParam(name = "order", defaultValue = "desc", required = false) String sortingOrder,
                                @RequestParam(name = "keyword", defaultValue = "", required = false) String keyword,
                                @RequestParam(name = "start-date", defaultValue = "", required = false) String startDate,
                                @RequestParam(name = "end-date", defaultValue = "", required = false) String endDate,
                                Model model) {
        Page<Post> posts;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        boolean isDurationAvailable = !startDate.isEmpty() || !endDate.isEmpty();

        if (startDate.isEmpty()) {
            startDate = "1970-01-01";
        }
        if (endDate.isEmpty()) {
            endDate = LocalDate.now().toString();
        }
        startDateTime = LocalDate.parse(startDate).atStartOfDay();
        endDateTime = LocalDate.parse(endDate).plusDays(1).atStartOfDay();

        if (!keyword.isEmpty()) {
            posts = postService.findPostsByKeyword(keyword, startDateTime, endDateTime, sortingField, sortingOrder, page, size);
        } else if (isDurationAvailable) {
            posts = postService.findPostsByDuration(startDateTime, endDateTime, sortingField, sortingOrder, page, size);
        } else {
            posts = postService.findAllPost(sortingField, sortingOrder, page, size);
        }
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sortingField", sortingField);
        model.addAttribute("sortingOrder", sortingOrder);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("totalPages", posts.getTotalPages());
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
    String savePost(@ModelAttribute("post") Post post, @RequestParam(name = "tag-string") String tagString) {
        if (post.getCreatedAt() == null) {
            post.setCreatedAt(LocalDateTime.now());
            post.setPublishedAt(LocalDateTime.now());
        } else {
            post.setUpdatedAt(LocalDateTime.now());
        }
        List<Tag> tagList = new ArrayList<>();
        for (String tagName : tagString.split(" ")) {
            Tag tag = tagService.findTagByName(tagName);
            if (Objects.isNull(tag)) {
                tag = new Tag();
                tag.setName(tagName);
                tag.setCreatedAt(LocalDateTime.now());
            }
            tagList.add(tag);
        }
        post.setTags(tagList);
        postService.savePost(post);
        return "redirect:/";
    }

    @GetMapping("delete-post")
    String deletePostById(@RequestParam("id") Integer id) {
        postService.deletePostById(id);
        return "redirect:/";
    }

    @GetMapping("edit-post")
    String getPostForUpdate(@RequestParam("id") Integer id, Model model) {
        Post post = postService.findPostById(id);
        StringBuilder tags = new StringBuilder();
        for (Tag tag : post.getTags()) {
            tags.append(tag.getName()).append(" ");
        }
        model.addAttribute("post", post);
        model.addAttribute("tags", tags.toString());
        return "post-form";
    }

    @GetMapping("new-post")
    String showFormForNewPost(Model model) {
        model.addAttribute("post", new Post());
        return "post-form";
    }
}
