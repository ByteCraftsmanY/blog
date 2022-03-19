package com.yogesh.blog.repositories;

import com.yogesh.blog.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {

}
