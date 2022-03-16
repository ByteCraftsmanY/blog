package com.yogesh.blog.services;

import com.yogesh.blog.entities.BlogPost;
import com.yogesh.blog.repositories.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {
    private final BlogPostRepository blogPostRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    public void addPost(BlogPost blogPost){
        blogPostRepository.save(blogPost);
    }

    public Optional<BlogPost> getPost(Integer id){
        return blogPostRepository.findById(id);
    }

    public List<BlogPost> getBlogPostList(){
        return blogPostRepository.findAll();
    }
}
