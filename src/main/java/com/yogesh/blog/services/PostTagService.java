package com.yogesh.blog.services;

import com.yogesh.blog.entities.PostTag;
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

    public void savePostTag(PostTag postTag){
        postTagRepository.save(postTag);
    }

    public PostTag fetchPostTag(Integer id){
        return postTagRepository.getById(id);
    }


}
