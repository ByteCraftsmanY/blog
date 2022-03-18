package com.yogesh.blog.repositories;

import com.yogesh.blog.entities.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag,Integer> {
}
