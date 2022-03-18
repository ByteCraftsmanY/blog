package com.yogesh.blog.services;

import com.yogesh.blog.entities.Tag;
import com.yogesh.blog.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public void saveTag(Tag tag){
        tagRepository.save(tag);
    }
}
