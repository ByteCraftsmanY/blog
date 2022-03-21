package com.yogesh.blog.repositories;

import com.yogesh.blog.entities.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(nativeQuery = true, value = "select * from posts where (content like %?1% or author like %?1% or excerpt like %?1% or title like %?1%) and (published_at between ?2 and ?3)")
    List<Post> findPostsByCriteria(String keyword, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);
}
