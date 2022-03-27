package com.yogesh.blog.controllers;

import com.yogesh.blog.exceptions.EntityNotFoundException;
import com.yogesh.blog.exceptions.UnauthorizedException;
import com.yogesh.blog.models.*;
import com.yogesh.blog.services.PostService;
import com.yogesh.blog.services.TagService;
import com.yogesh.blog.services.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final TagService tagService;
    private final UserPrincipalService userPrincipalService;

    @Autowired
    public PostController(PostService postService, TagService tagService, UserPrincipalService userPrincipalService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userPrincipalService = userPrincipalService;
    }

    @GetMapping
    List<Post> getAllPosts(@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
                           @RequestParam(name = "size", defaultValue = "5", required = false) Integer size,
                           @RequestParam(name = "sort-field", defaultValue = "publishedAt", required = false) String sortingField,
                           @RequestParam(name = "order", defaultValue = "desc", required = false) String sortingOrder,
                           @RequestParam(name = "keyword", defaultValue = "", required = false) String keyword,
                           @RequestParam(name = "start-date", defaultValue = "", required = false) String startDate,
                           @RequestParam(name = "end-date", defaultValue = "", required = false) String endDate,
                           @RequestParam(name = "author", defaultValue = "", required = false) String author,
                           @RequestParam(name = "selected-tags", defaultValue = "", required = false) List<String> selectedTags) {
        Page<Post> posts;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        boolean isPublishedDateDurationAvailable = !startDate.isEmpty() || !endDate.isEmpty();

        if (startDate.isEmpty()) {
            startDate = "1970-01-01";
        }
        if (endDate.isEmpty()) {
            endDate = LocalDate.now().toString();
        }
        startDateTime = LocalDate.parse(startDate).atStartOfDay();
        endDateTime = LocalDate.parse(endDate).plusDays(1).atStartOfDay();

        if (!keyword.isEmpty() && !author.isEmpty() && !selectedTags.isEmpty()) {
            posts = postService.findPostsByKeywordAndAuthorAndTagAndPublishedDateDuration(keyword, author, selectedTags, startDateTime, endDateTime, sortingField, sortingOrder, page, size);
        } else if (!keyword.isEmpty() && !author.isEmpty()) {
            posts = postService.findPostsByKeywordAndAuthorAndPublishedDateDuration(keyword, author, startDateTime, endDateTime, sortingField, sortingOrder, page, size);
        } else if (!keyword.isEmpty() && !selectedTags.isEmpty()) {
            posts = postService.findPostsByKeywordAndTagsAndPublishedDateDuration(keyword, selectedTags, startDateTime, endDateTime, sortingField, sortingOrder, page, size);
        } else if (!keyword.isEmpty()) {
            posts = postService.findPostsByKeywordAndPublishedDateDuration(keyword, startDateTime, endDateTime, sortingField, sortingOrder, page, size);
        } else if (!author.isEmpty() && !selectedTags.isEmpty()) {
            posts = postService.findPostByAuthorAndTagsAndPublishedDateDuration(author, selectedTags, startDateTime, endDateTime, sortingField, sortingOrder, page, size);
        } else if (!author.isEmpty()) {
            posts = postService.findPostsByAuthorAndPublishedDateDuration(author, startDateTime, endDateTime, sortingField, sortingOrder, page, size);
        } else if (!selectedTags.isEmpty()) {
            posts = postService.findPostByTagsAndPublishedDateDuration(selectedTags, startDateTime, endDateTime, sortingField, sortingOrder, page, size);
        } else if (isPublishedDateDurationAvailable) {
            posts = postService.findPostsByPublishedDateDuration(startDateTime, endDateTime, sortingField, sortingOrder, page, size);
        } else {
            posts = postService.findAllPost(sortingField, sortingOrder, page, size);
        }
        return posts.getContent();
    }

    @GetMapping("{id}")
    Post getPostById(@PathVariable Integer id) {
        return postService.findPostById(id).orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    @Secured({"ROLE_ADMIN", "ROLE_AUTHOR"})
    @PostMapping
    Post savePost(@RequestBody Post post) {
        List<Tag> postTags = post.getTags();
        List<String> tagNames = new ArrayList<>();
        for (Tag tag : postTags) {
            String name = tag.getName();
            tagNames.add(name);
        }
        Set<String> uniqueTagNames = new HashSet<>(tagNames);
        postTags = uniqueTagNames.stream().map(uniqueTagName -> {
            Tag tag = tagService.findTagByName(uniqueTagName).orElseGet(Tag::new);
            tag.setName(uniqueTagName);
            return tag;
        }).collect(Collectors.toList());
        post.setCreatedAt(LocalDateTime.now());
        post.setPublishedAt(LocalDateTime.now());
        post.setUser(Objects.requireNonNull(userPrincipalService.getUserPrincipal()).getUser());
        post.setTags(postTags);
        postService.savePost(post);
        return post;
    }

    @Secured({"ROLE_ADMIN", "ROLE_AUTHOR"})
    @DeleteMapping("{id}")
    void deletePostById(@PathVariable Integer id) {
        Post post = postService.findPostById(id).orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (!userPrincipalService.isUserAuthorizedForPostOperation(post)) {
            throw new UnauthorizedException("You are not authorized to delete this Post.");
        }
        postService.deletePostById(id);
    }

    @PatchMapping("{id}")
    public Post updatePostById(@PathVariable Integer id, @RequestBody Map<Object, Object> postFields) {
        Post post = postService.findPostById(id).orElseThrow(() -> new EntityNotFoundException("Post Not Found."));
        if (!userPrincipalService.isUserAuthorizedForPostOperation(post)) {
            throw new UnauthorizedException("You are not authorized to do this task.");
        }
//        ToDo: Solve Tag update problem
        postFields.forEach((key, value) -> {
            if (key.equals("tags")) {
                return;
            }
            Field field = ReflectionUtils.findField(Post.class, (String) key);
            Objects.requireNonNull(field).setAccessible(true);
            ReflectionUtils.setField(field, post, value);
        });
        post.setUpdatedAt(LocalDateTime.now());
        return postService.savePost(post);
    }
}
