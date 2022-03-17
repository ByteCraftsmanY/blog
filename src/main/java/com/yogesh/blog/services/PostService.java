package com.yogesh.blog.services;

import com.yogesh.blog.entities.Post;
import com.yogesh.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void addPost(Post blogPost){
        postRepository.save(blogPost);
    }

    public Optional<Post> getPost(Integer id){
        return postRepository.findById(id);
    }

    public List<Post> getBlogPostList(Integer page, String orderBy){
        Sort sort = Sort.by("publishedAt");
        if(orderBy.equals("asc")) {
            sort = sort.ascending();
        }else {
            sort = sort.descending();
        }
        return postRepository.findAll(PageRequest.of(page,2,sort)).getContent();
    }

    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }
}
