package com.yogesh.blog.repositories;

import com.yogesh.blog.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findTagByName(String tagName);
}
