package com.yogesh.blog.services;

import com.yogesh.blog.entities.Post;
import com.yogesh.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Post> getBlogPostList(){
        return postRepository.findAll();
    }

    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }
}
