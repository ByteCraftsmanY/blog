package com.yogesh.blog.repositories;

import com.yogesh.blog.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("select tag from Tag tag where tag.name = :tagName")
    Tag findTagByName(String tagName);
}
