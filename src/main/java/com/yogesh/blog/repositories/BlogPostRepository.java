package com.yogesh.blog.repositories;

import com.yogesh.blog.entities.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost,Integer> {
}
