package com.yogesh.blog.services;

import com.yogesh.blog.repositories.PostTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostTagService {
    private final PostTagRepository postTagRepository;

    @Autowired
    public PostTagService(PostTagRepository postTagRepository) {
        this.postTagRepository = postTagRepository;
    }
}
