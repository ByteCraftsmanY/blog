package com.yogesh.blog.services;

import com.yogesh.blog.entities.Post;
import com.yogesh.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void addPost(Post post) {
        postRepository.save(post);
    }

    public Optional<Post> getPost(Integer id) {
        return postRepository.findById(id);
    }

    public List<Post> getPostList(String sortingField, String sortingOrder, Integer start, Integer limit) {
        return postRepository.findPostsByLimit(start, limit);
    }

    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }

    public List<Post> searchPost(String keyword) {
        Set<Post> posts = new HashSet<>();
        posts.addAll(postRepository.findPostsWithKeyword(keyword));
        posts.addAll(postRepository.findPostWithTagKeyword(keyword));
        return new ArrayList<>(posts);
    }
}
