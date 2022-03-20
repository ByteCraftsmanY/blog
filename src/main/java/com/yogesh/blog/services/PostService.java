package com.yogesh.blog.services;

import com.yogesh.blog.entities.Post;
import com.yogesh.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }

    public Post findPostById(Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> findAllPost(String sortingField, String sortingOrder, Integer page, Integer item) {
        Sort sort = Sort.by(sortingField);
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findAll(PageRequest.of(page,item,sort)).getContent();
    }

    public void deletePostById(Integer id) {
        postRepository.deleteById(id);
    }

    public List<Post> findPostsWithCriteria(List<String> tagList, String author, LocalDateTime startDateTime, LocalDateTime endDateTime){
        return postRepository.findPostsWithCriteria(author,startDateTime,endDateTime,tagList);
    }

    public List<Post> findPostsHavingKeyword(String keyword) {
        Set<Post> posts = new HashSet<>();
        posts.addAll(postRepository.findPostsHavingTag(keyword));
        posts.addAll(postRepository.findPostsHavingKeyword(keyword));
        return new ArrayList<>(posts);
    }
}
