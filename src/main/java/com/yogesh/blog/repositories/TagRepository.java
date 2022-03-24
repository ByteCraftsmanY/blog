package com.yogesh.blog.repositories;

import com.yogesh.blog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("select tag from Tag tag where tag.name = :tagName")
    Tag findTagByName(String tagName);

    @Query("select tag.name from Tag tag")
    List<String> findAllTagNames();
}
