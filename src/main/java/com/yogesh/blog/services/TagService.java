package com.yogesh.blog.services;

import com.yogesh.blog.model.Tag;
import com.yogesh.blog.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag findTagByName(String tagName) {
        return tagRepository.findTagByName(tagName);
    }

    public List<String> findAllTagNames(){
        return tagRepository.findAllTagNames();
    }
}
