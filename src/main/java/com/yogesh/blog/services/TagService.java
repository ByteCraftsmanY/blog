package com.yogesh.blog.services;

import com.yogesh.blog.models.Tag;
import com.yogesh.blog.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Optional<Tag> findTagByName(String tagName) {
        return tagRepository.findTagByName(tagName);
    }
}
